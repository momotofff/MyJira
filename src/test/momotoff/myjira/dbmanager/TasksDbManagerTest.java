package momotoff.myjira.dbmanager;

import io.swagger.model.Task;
import io.swagger.model.TaskPriority;
import io.swagger.model.TaskStatus;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksDbManagerTest extends DbManagerTestFixture
{
    private final String title = "Title1";
    private final String description = "Description for Task 1";
    private final String status = TaskStatus.PENDING.getValue();
    private final String priority = TaskPriority.HIGH.getValue();

    private final String userNameAssign = "userNameAssign";
    private final String roleAssign = UserRole.USER.toString();
    private final String emailAssign = "userNameAssign@example.com";

    @BeforeEach
    public void beforeEach()
    {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        containerDelegate.execute("DELETE FROM tasks", "", 0, true, true);
        containerDelegate.execute("DELETE FROM users", "", 0, true, true);
    }

    @Test
    public void createAndGetTask_ExpectSuccess() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        Task task = databaseManager.createTask(title, description, status, priority, user.getId());

        List<Task> tasks = databaseManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getTitle(), task.getTitle());
        assertEquals(tasks.get(0).getDescription(), task.getDescription());
        assertEquals(tasks.get(0).getStatus(), task.getStatus());
        assertEquals(tasks.get(0).getPriority(), task.getPriority());
    }

    @Test
    public void createTaskByAnInvalidUser_ExpectFailed()
    {
        final Long InvalidUserId = Long.MAX_VALUE;

        SQLException thrown = assertThrows(SQLException.class, () -> {
            databaseManager.createTask(title, description, status, priority, InvalidUserId);
        });

        assertEquals("User with ID " + InvalidUserId + " does not exist", thrown.getMessage());
    }

    @Test
    public void getTasksByAuthorId_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        List<Task> tasks = assertDoesNotThrow(() -> databaseManager.getTasksByAuthorId(user.getId()));

        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getTitle(), title);
        assertEquals(tasks.get(0).getDescription(), description);
        assertEquals(tasks.get(0).getStatus().getValue(), status);
        assertEquals(tasks.get(0).getPriority().getValue(), priority);
    }

    @Test
    public void getTasksByInvalidAuthorId_ExpectEmptyList() throws SQLException
    {
        final long InvalidUserId = Long.MAX_VALUE;
        User user = databaseManager.createUser(username, role, email);

        assertNotNull(user);
        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertEquals(0, databaseManager.getTasksByAuthorId(InvalidUserId).size());
    }

    @Test
    public void getTasksByAssigneeId_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        User assignee = databaseManager.createUser(userNameAssign, roleAssign, emailAssign);
        assertNotNull(assignee);

        Task task = assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertDoesNotThrow(() -> databaseManager.updateTask(task.getId(), title, description, status, priority, assignee.getId()));
        List<Task> tasks = assertDoesNotThrow(() -> databaseManager.getTasksByAssigneeId(assignee.getId()));

        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getTitle(), title);
        assertEquals(tasks.get(0).getDescription(), description);
        assertEquals(tasks.get(0).getStatus().getValue(), status);
        assertEquals(tasks.get(0).getPriority().getValue(), priority);
    }

    @Test
    public void UpdateTask_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        User assignee = databaseManager.createUser(userNameAssign, roleAssign, emailAssign);
        assertNotNull(assignee);

        Task task = assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

        final String newTitle = "WAAAGH";
        final String newDescription = "For the Gorka and Morka";
        final String newStatus = TaskStatus.ACTIVE.getValue();
        final String newPriority = TaskPriority.MEDIUM.getValue();
        Task updated = assertDoesNotThrow(() -> databaseManager.updateTask(task.getId(), newTitle, newDescription, newStatus, newPriority, assignee.getId()));

        assertEquals(newTitle, updated.getTitle());
        assertEquals(newDescription, updated.getDescription());
        assertEquals(newStatus, updated.getStatus().getValue());
        assertEquals(newPriority, updated.getPriority().getValue());
    }

    @Test
    public void UpdateTask_ExpectFailed() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        User assignee = databaseManager.createUser(userNameAssign, roleAssign, emailAssign);
        assertNotNull(assignee);

        Task task = assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

        final String newTitle = "WAAAGH";
        final String newDescription = "For the Gorka and Morka";
        final String newStatus = "Reserve";
        final String newPriority = "VeryHard";
        assertThrows(SQLException.class, () -> databaseManager.updateTask(task.getId(), newTitle, newDescription, newStatus, newPriority, assignee.getId()));
    }

    @Test
    public void getTaskById_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        long taskId = (assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()))).getId();
        Task task = assertDoesNotThrow(() -> databaseManager.getTaskById(taskId));

        assertNotNull(task);
        assertEquals(task.getTitle(), title);
        assertEquals(task.getDescription(), description);
        assertEquals(task.getStatus().getValue(), status);
        assertEquals(task.getPriority().getValue(), priority);
        assertEquals(task.getAuthor(), user.getId());
    }

    @Test
    public void getTaskByInvalidId_ExpectEmpty() throws SQLException
    {
        final long InvalidTaskId = Long.MAX_VALUE;
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertDoesNotThrow(() -> databaseManager.getTaskById(InvalidTaskId));

    }

    @Test
    public void deleteTaskById_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        long taskId = assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId())).getId();

        assertDoesNotThrow(() -> databaseManager.deleteTaskById(taskId));
        assertNull(databaseManager.getTaskById(taskId));
    }

    @Test
    public void deleteTaskById_ExpectFailed() throws SQLException
    {
        final long InvalidTaskId = Long.MAX_VALUE;
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> databaseManager.deleteTaskById(InvalidTaskId));
        assertEquals("Task with ID " + InvalidTaskId + " does not exist.", runtimeException.getMessage());
    }

    @Test
    public void searchTasks_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertDoesNotThrow(() -> databaseManager.createTask(title, "description", status, priority, user.getId()));
        assertDoesNotThrow(() -> databaseManager.createTask(title, "new test task", status, priority, user.getId()));

        List<Task> returnedTasks = assertDoesNotThrow(() -> databaseManager.getTasksByAuthorId(user.getId()));
        assertEquals(3, returnedTasks.size());
        assertEquals(2, databaseManager.searchTasks("description").size());
        assertEquals(2, databaseManager.searchTasks("task").size());
    }
    @Test
    public void searchTasksNullKeyword_ExpectThrows() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            databaseManager.searchTasks("");
        });

        assertEquals("Keyword cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void searchTasks_NoTask_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        List<Task> tasks = assertDoesNotThrow(() -> databaseManager.searchTasks("Fuck"));
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void getTasksByStatus_ExpectSuccess() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertEquals(1, databaseManager.getTasksByStatus(status).size());
    }

    @Test
    public void getTasksByStatus_ExpectThrows() throws SQLException
    {
        final String badStatus = "WAAAAGH";
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByStatus(badStatus));
        assertEquals("Bad task status: " + badStatus, exception.getMessage());
    }

    @Test
    public void getTasksByStatus_NoTaskFound_ExpectCorrect() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertTrue(databaseManager.getTasksByStatus(TaskStatus.RESOLVED.getValue()).isEmpty());
    }

    @Test
    public void getTasksByPriority_ExpectSuccess() throws SQLException, IOException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertEquals(1, databaseManager.getTasksByPriority(priority).size());
    }

    @Test
    public void getTasksByPriority_ExpectThrows() throws SQLException
    {
        final String badPriority = "WAAAAGH";
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByPriority(badPriority));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByPriority(null));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByPriority(""));
    }
}
