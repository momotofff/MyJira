package momotoff.myjira.dbmanager;

import io.swagger.model.Task;
import io.swagger.model.TaskPriority;
import io.swagger.model.TaskStatus;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class TasksDbManager
{
    public static List<Task> getTasksByAssigneeId(Connection connection, long assigneeId) throws SQLException
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE assignee = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, assigneeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                tasks.add(task);
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error retrieving tasks by assignee id", e);
        }

        return tasks;
    }

    public static Task createTask(Connection connection, String title, String description, String status, String priority, Long authorId) throws SQLException
    {
        String sql =
            "INSERT INTO tasks " +
            "(title, description, status,        priority,        author) VALUES " +
            "(?,     ?,           ?::taskstatus, ?::taskpriority, ?)";

        Task task = null;

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
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        long id = generatedKeys.getLong(1);
                        task = new Task();
                        task.setId(id);
                        task.setTitle(title);
                        task.setDescription(description);
                        task.setStatus(TaskStatus.fromValue(status));
                        task.setPriority(TaskPriority.fromValue(priority));
                        task.setAuthor(authorId);
                        System.out.printf("Task with ID = %d created successfully!%n", id);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            if (e.getMessage().contains("violates foreign key constraint"))
                throw new SQLException("User with ID " + authorId + " does not exist", e);

            throw new SQLException("Error creating task", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Invalid status or priority: " + e.getMessage());
        }

        return task;
    }

    public static List<Task> getTasks(Connection connection) throws SQLException
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
                task.setAssignee(JsonNullable.of(resultSet.getLong("assignee")));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                task.setAuthor(resultSet.getLong("author"));
                task.setDescription(resultSet.getString("description"));
                list.add(task);
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error retrieving tasks list", e);
        }

        return list;
    }

    public static Task getTaskById(Connection connection, long taskId) throws SQLException
    {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        Task task = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, taskId);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    task = new Task();
                    task.setId(resultSet.getLong("id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setAssignee(JsonNullable.of(resultSet.getLong("assignee")));
                    task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                    task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                    task.setAuthor(resultSet.getLong("author"));
                    task.setDescription(resultSet.getString("description"));
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error retrieving task by id", e);
        }

        return task;
    }

    public static Task updateTask(Connection connection, long taskId, String title, String description, String status, String priority, Long assigneeId) throws SQLException
    {
        String sql =
            "UPDATE tasks " +
            "SET title = ?, description = ?, status = ?::taskstatus, assignee = ?, priority = ?::taskpriority " +
            "WHERE id = ?";

        Task task = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.setLong(4, assigneeId);
            preparedStatement.setString(5, priority);
            preparedStatement.setLong(6, taskId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                task = new Task();
                task.setId(taskId);
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(TaskStatus.fromValue(status));
                task.setPriority(TaskPriority.fromValue(priority));
                task.setAssignee(JsonNullable.of(assigneeId));

                System.out.printf("Task with ID = %d updated successfully!%n", taskId);
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error updating task", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Invalid status or priority: " + e.getMessage());
        }

        return task;
    }

    public static void deleteTaskById(Connection connection, long id) throws SQLException
    {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0)
                throw new RuntimeException("Task with ID " + id + " does not exist.");

            if (affectedRows > 0)
                System.out.printf("Task with ID = %d deleted successfully!%n", id);
            else
                System.out.printf("No task found with ID = %d.%n", id);
        }
        catch (SQLException e)
        {
            throw new SQLException("Error deleting task", e);
        }
    }

    public static List<Task> getTasksByAuthorId(Connection connection, long authorId) throws SQLException
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE author = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));
                task.setAuthor(authorId);
                tasks.add(task);
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error retrieving tasks by author id", e);
        }

        return tasks;
    }

    public static List<Task> searchTasks(Connection connection, String keyword) throws SQLException
    {
        if (keyword == null || keyword.trim().isEmpty())
            throw new IllegalArgumentException("Keyword cannot be null or empty");

        List<Task> matchingTasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE LOWER(title) LIKE ? OR LOWER(description) LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            String searchKeyword = "%" + keyword.toLowerCase() + "%";

            preparedStatement.setString(1, searchKeyword);
            preparedStatement.setString(2, searchKeyword);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    Task task = new Task();
                    task.setId(resultSet.getLong("id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                    task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));

                    matchingTasks.add(task);
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Database error during task search", e);
        }

        return matchingTasks;
    }

    public static List<Task> getTasksByStatus(Connection connection, String status) throws SQLException
    {
        try
        {
            TaskStatus.fromValue(status);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Bad task status: " + status);
        }

        List<Task> matchingTasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE status = ?::taskstatus";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    Task task = new Task();
                    task.setId(resultSet.getLong("id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                    task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));

                    matchingTasks.add(task);
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Database error during task search by priority", e);
        }

        return matchingTasks;
    }

    public static List<Task> getTasksByPriority(Connection connection, String priority) throws SQLException
    {
        List<Task> list = new ArrayList<>();
        try
        {
            TaskPriority.fromValue(priority);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Bad task priority: " + priority);
        }

        String sql = "SELECT * FROM tasks WHERE priority = ?::taskpriority";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, priority);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    Task task = new Task();
                    task.setId(resultSet.getLong("id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setStatus(TaskStatus.fromValue(resultSet.getString("status")));
                    task.setPriority(TaskPriority.fromValue(resultSet.getString("priority")));

                    list.add(task);
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Database error during task search", e);
        }

        return list;
    }
}
