package momotoff.myjira.dbmanager;
import io.swagger.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private final String url;
    private final String user;
    private final String password;

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, user, password);
    }

    public DatabaseManager(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public User createUser(String username, String role, String email) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.createUser(connection, username, role, email);
    }

    public List<User> getUsers() throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.getUsers(connection);
    }

    public User getUserByName(String name) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.getUserByName(connection, name);
    }

    public Task createTask(String title, String description, String status, String priority, String author, String assignee)
    {
        String sql =
            "INSERT INTO tasks " +
            "(title, description, status,        priority,        author, assignee) VALUES " +
            "(?,     ?,           ?::taskstatus, ?::taskpriority, ?,      ?)";

        Task task = null;
        long authorId;
        long assigneeId;

        try
        {
            authorId = getUserIdByUsername(author);
            assigneeId = getUserIdByUsername(assignee);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.setString(4, priority);
            preparedStatement.setLong(5, authorId);
            preparedStatement.setLong(6, assigneeId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        task = new Task();
                        task.setId(id);
                        task.setTitle(title);
                        task.setDescription(description);
                        task.setStatus(TaskStatus.fromValue(status));
                        task.setPriority(TaskPriority.fromValue(priority));
                        task.setAuthor(author);
                        task.setAssignee(assignee);
                        System.out.printf("Task with ID = %d created successfully!%n", id);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error creating task: " + e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Invalid status or priority: " + e.getMessage());
        }

        return task;
    }

    public List<Task> getTasks() throws SQLException
    {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setAssignee(resultSet.getString("assignee"));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                task.setAuthor(resultSet.getString("author"));
                task.setDescription(resultSet.getString("description"));
                list.add(task);
            }
        }

        catch (SQLException e)
        {
            System.err.println("Error retrieving tasks: " + e.getMessage());
        }

        return list;
    }

    public User updateUser(String username, UpdateUserRequest body)
    {
        String sql = "UPDATE users SET role = ?, email = ? WHERE username = ?";
        User updatedUser = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {

            preparedStatement.setString(1, body.getRole().getValue());
            preparedStatement.setString(2, body.getEmail());
            preparedStatement.setString(3, username);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                updatedUser = new User();
                updatedUser.setUsername(username);
                updatedUser.setRole(body.getRole());
                updatedUser.setEmail(body.getEmail());
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error updating user: " + e.getMessage());
        }

        return updatedUser;
    }

    private long getUserIdByUsername(String userName) throws SQLException
    {
        String sql = "SELECT * FROM users WHERE userName = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getLong("id");
            else
                throw new SQLException("User not found: " + userName);
        }
    }

    public String getUserNameByUserId(String userId)
    {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            long id = Long.parseLong(userId);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getString("userName");
            else
                throw new SQLException("User not found: " + userId);

        }

        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid user ID: " + userId, e);
        }

        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getTasksByUserName(String userName)
    {
        String sql = "SELECT * FROM tasks WHERE assignee = ?";
        List<Task> list = new ArrayList<>();

        long assigneeId;

        try
        {
            assigneeId = getUserIdByUsername(userName);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }


        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, assigneeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setAssignee(resultSet.getString("assignee"));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                task.setAuthor(resultSet.getString("author"));
                task.setDescription(resultSet.getString("description"));
                list.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isUserExists(String userName)
    {
        String sql = "SELECT COUNT(*) FROM users WHERE userName = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {

            preparedStatement.setString(1, userName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                    return resultSet.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void deleteUserByUserName(String userName)
    {
        if (!isUserExists(userName))
            return;

        String sql = "DELETE FROM users WHERE userName = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate(); // Выполнение запроса на удаление
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
