package momotoff.myjira.dbmanager;
import io.swagger.model.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
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

    public User updateUserByName(String username, UpdateUserRequest body) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.updatingUserByName(connection, username, body);
    }

    public User updateUserById(long userd, UpdateUserRequest body) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.updatingUserById(connection, userd, body);
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

    public User getUserById(long id) throws SQLException
    {
        Connection connection = getConnection();
        return UsersDbManager.getUserById(connection, id);
    }

    public void deleteUserByName(String userName) throws SQLException
    {
        Connection connection = getConnection();
        UsersDbManager.deleteUserByName(connection, userName);
    }
    public void deleteUserById(Long userId) throws SQLException
    {
        Connection connection = getConnection();
        UsersDbManager.deleteUserById(connection, userId);
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

    public List<Task> getTasks() throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasks(connection);
    }

    public Task getTaskById(long taskId) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTaskById(connection, taskId);
    }

    public Task updateTask(long taskId, String title, String description, String status, String priority, Long assigneeId) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.updateTask(connection, taskId, title, description, status, priority, assigneeId);
    }

    public void deleteTaskById(long id) throws SQLException
    {
        Connection connection = getConnection();
        TasksDbManager.deleteTaskById(connection, id);
    }

    public List<Task> getTasksByAssigneeId(long assigneeId) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByAssigneeId(connection, assigneeId);
    }

    public List<Task> getTasksByAuthorId(long userid) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByAuthorId(connection, userid);
    }

    public List<Task> searchTasks(String keyword) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.searchTasks(connection, keyword);
    }

    public List<Task> getTasksByStatus(String status) throws SQLException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByStatus(connection, status);
    }

    public List<Task> getTasksByPriority(String priority) throws SQLException, IOException
    {
        Connection connection = getConnection();
        return TasksDbManager.getTasksByPriority(connection, priority);
    }
}
