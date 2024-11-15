package momotoff.myjira.dbmanager;
import io.swagger.model.*;
import java.sql.*;
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

    public Task createTask(String title, String description, String status, String priority) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.createTask(connection, title, description, status, priority);
    }

    public Task updateTask(long taskId, String title, String description, String status, String priority) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.updateTask(connection, taskId, title, description, status, priority);
    }

    public List<Task> getTasks() throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasks(connection);
    }

    public User updateUser(String username, UpdateUserRequest body) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.updateUser(connection, username, body);
    }

    public String getUserNameByUserId(String userId) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.getUserNameByUserId(connection, userId);
    }

    public List<Task> getTasksByUserName(String userName) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByUserName(connection, userName);
    }

    public void deleteUserByUserName(String userName) throws SQLException
    {
        Connection connection = getConnection();
        UsersDbManager.deleteUserByUserName(connection, userName);
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
}
