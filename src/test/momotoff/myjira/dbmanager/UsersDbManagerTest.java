package momotoff.myjira.dbmanager;

import io.swagger.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDbManagerTest extends DbManagerTestFixture
{
    @BeforeEach
    public void beforeEach()
    {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        containerDelegate.execute("DELETE FROM users", "", 0, true, true);
    }

    @Test
    public void createAndGetUser_ExpectSuccess() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(role, user.getRole().toString());
        assertEquals(email, user.getEmail());

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
        assertEquals(users.get(0).getUsername(), user.getUsername());
        assertEquals(users.get(0).getRole(), user.getRole());
        assertEquals(users.get(0).getEmail(), user.getEmail());
    }

    @Test
    public void createUserWithDuplicateUsername_ExpectFailed() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        assertThrows(SQLException.class, () -> databaseManager.createUser(username, role, email));

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    void deleteUserByUserName_ExpectSuccess()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should exist after creation");

        assertDoesNotThrow(() -> databaseManager.deleteUserByUserName(username));
        assertFalse(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should be deleted");
    }

    @Test
    void getUserByName_ExpectSuccess()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should exist after creation");

        assertNotNull(assertDoesNotThrow(() -> databaseManager.getUserByName(username)));
    }

    @Test
    void getUserByName_ExpectFailed()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should exist after creation");

        assertNull(assertDoesNotThrow(() -> databaseManager.getUserByName("Emperor")));
    }
}