package momotoff.myjira.dbmanager;

import io.swagger.model.Task;
import io.swagger.model.TaskPriority;
import io.swagger.model.TaskStatus;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class TasksDbManager
{
    public static List<Task> getTasksByUserName(Connection connection, String userName)
    {
        String sql = "SELECT * FROM tasks WHERE assignee = ?";
        List<Task> list = new ArrayList<>();

        long assigneeId;

        try
        {
            assigneeId = getUserIdByUsername(connection, userName);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, assigneeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setAssignee(JsonNullable.of(resultSet.getString("assignee")));
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

    public static Task createTask(Connection connection, String title, String description, String status, String priority)
    {
        String sql =
                "INSERT INTO tasks " +
                        "(title, description, status,        priority,        author, assignee) VALUES " +
                        "(?,     ?,           ?::taskstatus, ?::taskpriority, ?,      DEFAULT)";

        Task task = null;
        long authorId;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        try
        {
            authorId = getUserIdByUsername(connection, author);
        }

        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.setString(4, priority);
            preparedStatement.setLong(5, authorId);

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

    public static List<Task> getTasks(Connection connection)
    {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setAssignee(JsonNullable.of(resultSet.getString("assignee")));
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

    public static Task updateTask(Connection connection, long taskId, String title, String description, String status, String priority) {
        String sql =
                "UPDATE tasks SET title = ?, description = ?, status = ?::taskstatus, " +
                        "priority = ?::taskpriority, author = ? WHERE id = ?";

        Task task = null;
        long authorId;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        try
        {
            authorId = getUserIdByUsername(connection, author);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.setString(4, priority);
            preparedStatement.setLong(5, authorId); // Установка id автора
            preparedStatement.setLong(6, taskId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                task = new Task();
                task.setId(taskId);
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(TaskStatus.fromValue(status));
                task.setPriority(TaskPriority.fromValue(priority));
                task.setAuthor(author);
                // assignee остается без изменений, если не нужно его обновлять
                System.out.printf("Task with ID = %d updated successfully!%n", taskId);
            }
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status or priority: " + e.getMessage());
        }

        return task;
    }


    private static long getUserIdByUsername(Connection connection, String userName) throws SQLException
    {
        String sql = "SELECT * FROM users WHERE userName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getLong("id");
            else
                throw new SQLException("User not found: " + userName);
        }
    }
}
