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
    private final String title = "Tas2324";
    private final String description = "Description for Task 1";
    private final String status = TaskStatus.PENDING.getValue();
    private final String priority = TaskPriority.HIGH.getValue();
    private final Long unauthorizedUserId = 5L;

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
    public void createTaskByAnUnauthorizedUser_ExpectFailed()
    {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            databaseManager.createTask(title, description, status, priority, unauthorizedUserId);
        });

        assertEquals("User with ID " + unauthorizedUserId + " does not exist.", thrown.getMessage());
    }

    @Test
    public void getTasksByAuthorId() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

        List<Task> returnedTasks = assertDoesNotThrow(() -> databaseManager.getTasksByAuthorId(user.getId()));

        for (Task task : returnedTasks)
        {
            assertEquals(task.getTitle(), title);
            assertEquals(task.getDescription(), description);
            assertEquals(task.getStatus().getValue(), status);
            assertEquals(task.getPriority().getValue(), priority);
        }
    }

    @Test
    public void getTasksByAssigneeId() throws SQLException {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        User assignUser = databaseManager.createUser(userNameAssign, roleAssign, emailAssign);
        assertNotNull(assignUser);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        List<Task> returnedTasks = assertDoesNotThrow(() -> databaseManager.getTasksByAssigneeId(assignUser.getId()));

        for (Task task : returnedTasks) {
            assertEquals(task.getTitle(), title);
            assertEquals(task.getDescription(), description);
            assertEquals(task.getStatus().getValue(), status);
            assertEquals(task.getPriority().getValue(), priority);

        }
    }

    @Test
    public void UpdatedTask() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        User assignUser = databaseManager.createUser(userNameAssign, roleAssign, emailAssign);
        assertNotNull(assignUser);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

    }

    @Test
    public void getTasks() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));

        List<Task> returnedTasks = assertDoesNotThrow(() -> databaseManager.getTasksByAuthorId(user.getId()));
        assertEquals(1, returnedTasks.size());
    }

    @Test
    public void getTaskById() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        long taskId = (assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()))).getId();
        assertNotNull(assertDoesNotThrow(() -> databaseManager.getTaskById(taskId)));
    }

    @Test
    public void updateTask()
    {

    }

    @Test
    public void deleteTaskById() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        long taskId = (assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()))).getId();

        assertDoesNotThrow(() -> databaseManager.deleteTaskById(taskId));
        assertNull(databaseManager.getTaskById(taskId));
    }

    @Test
    public void searchTasks() throws SQLException, IOException
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
    public void getTasksByStatus_ExpectSuccess() throws SQLException, IOException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertEquals(1, databaseManager.getTasksByStatus(status).size());
    }

    @Test
    public void getTasksByStatus_ExpectFailed_IOException() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertThrows(IOException.class, () -> databaseManager.getTasksByStatus("status"));
    }

    @Test
    public void getTasksByStatus_ExpectFailed() throws SQLException, IOException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertEquals(0, databaseManager.getTasksByStatus("Resolved").size());
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
    public void getTasksByPriority_ExpectFailed_IOException() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertThrows(IOException.class, () -> databaseManager.getTasksByPriority("priority"));
    }

    @Test
    public void getTasksByPriority_ExpectFailed_IllegalArgumentException() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertDoesNotThrow(() -> databaseManager.createTask(title, description, status, priority, user.getId()));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByPriority(null));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.getTasksByPriority(""));
    }
}
