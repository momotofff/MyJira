package momotoff.myjira.dbmanager;

import io.swagger.model.Task;
import io.swagger.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksDbManagerTest extends DbManagerTestFixture
{
    private final String title = "Tas2324";
    private final String description = "Description for Task 1";
    private final String status = "Pending";
    private final String priority = "High";
    private final Long unauthorizedUserId = 5L;

    @BeforeEach
    public void beforeEach()
    {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        containerDelegate.execute("DELETE FROM tasks", "", 0, true, true);
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
    public void getTasksByAuthorName()
    {

    }

    @Test
    public void getTasksByAuthorId()
    {

    }

    @Test
    public void getTasksByAssigneeName()
    {

    }

    @Test
    public void getTasksByAssigneeId()
    {

    }
}
