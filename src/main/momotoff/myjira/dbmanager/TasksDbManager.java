package momotoff.myjira.dbmanager;

import io.swagger.model.Task;
import io.swagger.model.TaskPriority;
import io.swagger.model.TaskStatus;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;
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
            e.printStackTrace();
        }

        return list;
    }

    public static Task createTask(Connection connection, String title, String description, String status, String priority, Long authorId)
    {
        if (!isUserExists(connection, authorId))
            throw new IllegalArgumentException("User with ID " + authorId + " does not exist.");

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
            System.err.println("Error retrieving tasks: " + e.getMessage());
        }

        return list;
    }

    public static Task updateTask(Connection connection, long taskId, String title, String description, String status, String priority, Long assigneeId)
    {
        String sql =
            "UPDATE tasks" +
            "SET title = ?, description = ?, status = ?::taskstatus, assignee = ?, priority = ?::taskpriority" +
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
            System.err.println("Error updating task: " + e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
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

    public static void deleteTaskById(Connection connection, long id)
    {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
                System.out.printf("Task with ID = %d deleted successfully!%n", id);
            else
                System.out.printf("No task found with ID = %d.%n", id);

        }
        catch (SQLException e)
        {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }

    private static boolean isUserExists(Connection connection, Long userId)
    {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                return resultSet.getInt(1) > 0;

        }
        catch (SQLException e)
        {
            System.err.println("Error checking if user exists: " + e.getMessage());
        }

        return false;
    }

    public static List<Task> getTasksByUserId(Connection connection, long userId)
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE assignee = ?"; // Предполагаем, что задачи присваиваются пользователям через поле assignee.

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setStatus(TaskStatus.fromValue(resultSet.getString("status"))); // Предполагаем, что у вас есть метод отрисовки статуса.
                task.setPriority(TaskPriority.fromValue(resultSet.getString("priority"))); // Идентифицируем приоритет.
                tasks.add(task);
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error retrieving tasks for user ID = " + userId + ": " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> getTasksByAuthorId(Connection connection, long userId)
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE creator_id = ?"; // Предполагаем, что у нас есть поле creator_id для идентификации автора.

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);
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
            System.err.println("Error retrieving tasks for author ID = " + userId + ": " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> getTasksByAuthorName(Connection connection, String userName)
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.* FROM tasks t JOIN users u ON t.creator_id = u.id WHERE u.username = ?"; // Соединяем таблицы для получения задач по имени автора.

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
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
            System.err.println("Error retrieving tasks for author name = " + userName + ": " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> searchTasks(Connection connection, String keyword) throws IOException
    {
        if (keyword == null || keyword.trim().isEmpty())
            throw new IllegalArgumentException("Keyword cannot be null or empty");


        List<Task> matchingTasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE LOWER(title) LIKE ? OR LOWER(description) LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            // Устанавливаем параметры запроса
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
        } catch (SQLException e) {
            throw new IOException("Database error during task search", e);
        }

        if (matchingTasks.isEmpty()) {
            throw new IOException("No tasks found matching the provided keyword");
        }

        return matchingTasks;
    }

    public static List<Task> getTasksByStatus(Connection connection, String status) throws IOException
    {
        List<Task> matchingTasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE status = ?";

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
            throw new IOException("Database error during task search", e);
        }

        if (matchingTasks.isEmpty())
            throw new IOException("No tasks found matching the provided status");

        return matchingTasks;
    }
}
