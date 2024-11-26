package momotoff.myjira.dbmanager;
import io.swagger.model.*;
import java.sql.*;
import java.util.List;

public class DatabaseManager
{
    private final String url;
    private final String user;
    private final String password;

    Connection getConnection() throws SQLException
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

    public User updateUser(String username, UpdateUserRequest body) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.updateUser(connection, username, body);
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

    public void deleteUserByUserName(String userName) throws SQLException
    {
        Connection connection = getConnection();
        UsersDbManager.deleteUserByUserName(connection, userName);
    }
    public void deleteUserByUserId(Long userId) throws SQLException
    {
        Connection connection = getConnection();
        UsersDbManager.deleteUserByUserId(connection, userId);
    }

    public long getUserByUserId(long userId) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.getUserNameByUserId(connection, userId);
    }

    public boolean isUserExists(String userName) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.isUserExists(connection, userName);
    }
    public boolean isUserExists(Long userId) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.isUserExists(connection, userId);
    }

    public Task createTask(String title, String description, String status, String priority, Long authorId) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.createTask(connection, title, description, status, priority, authorId);
    }

    public Task updateTask(long taskId, String title, String description, String status, String priority, Long assigneeId) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.updateTask(connection, taskId, title, description, status, priority, assigneeId);
    }

    public List<Task> getTasks() throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasks(connection);
    }

    public List<Task> getTasksByUserName(String userName) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByUserName(connection, userName);
    }

    public void deleteTaskById(long id) throws SQLException
    {
        Connection connection = getConnection();
        TasksDbManager.deleteTaskById(connection, id);
    }


}
