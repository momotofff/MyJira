package groovy.org.example;
import io.swagger.model.Task;
import io.swagger.model.UpdateUserRequest;
import io.swagger.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public User createUser(String username, String role, String email)
    {
        String sql = "INSERT INTO users (username, role, email) VALUES (?, ?, ?)";
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);
            preparedStatement.setString(3, email);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        user = new User();
                        user.setId(id);
                        user.setUsername(username);
                        user.setRole(User.RoleEnum.valueOf(role));
                        user.setEmail(email);
                        System.out.println("User created successfully!");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error creating user: " + e.getMessage());
        }

        return user;
    }

    public List<User> getUsers() throws SQLException
    {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            while (resultSet.next())
            {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(User.RoleEnum.fromValue(resultSet.getString("role")));
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }

        return list;
    }

    public User getUserByName(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(User.RoleEnum.fromValue(rs.getString("role")));
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error while trying to get user by name", e);
        }

        return user;
    }

    public void createTask(String title, String description, String status, String priority, String author, String assignee)
    {
        String sql = "INSERT INTO tasks (title, description, status, priority, author, assignee) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, status);
            pstmt.setString(4, priority);
            pstmt.setString(5, author);
            pstmt.setString(6, assignee);
            pstmt.executeUpdate();
            System.out.println("Task created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
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
                task.setStatus(Task.StatusEnum.fromValue(resultSet.getString("status")));
                task.setPriority(Task.PriorityEnum.fromValue(resultSet.getString("priority")));
                task.setAuthor(resultSet.getString("author")); // Исправил опечатку: "autor" на "author"
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

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql))
        {

            preparedStatement.setString(1, body.getRole());
            preparedStatement.setString(2, body.getEmail());
            preparedStatement.setString(3, username);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                updatedUser = new User();
                updatedUser.setUsername(username);
                updatedUser.setRole(User.RoleEnum.valueOf(body.getRole()));
                updatedUser.setEmail(body.getEmail());
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error updating user: " + e.getMessage());
        }

        return updatedUser;
    }
}
