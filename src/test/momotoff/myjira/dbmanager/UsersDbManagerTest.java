package momotoff.myjira.dbmanager;

import io.swagger.model.UpdateUserRequest;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import org.junit.jupiter.api.*;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.sql.*;
import java.util.List;

import static momotoff.myjira.dbmanager.UsersDbManager.isValidEmail;
import static momotoff.myjira.dbmanager.UsersDbManager.isValidRole;
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
    public void createAndGetUser_ExpectFailed()
    {
        String roleFailed = "roleFailed";
        SQLException exceptionRole = assertThrows(SQLException.class, () -> databaseManager.createUser(username, roleFailed, email));
        assertEquals("Invalid role: " + roleFailed, exceptionRole.getMessage());

        String emailFailed = "emailFailed";
        SQLException exceptionEmail = assertThrows(SQLException.class, () -> databaseManager.createUser(username, role, emailFailed));
        assertEquals("Invalid email: " + emailFailed, exceptionEmail.getMessage());
    }

    @Test
    public void createUserWithDuplicateUsername_ExpectFailed() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        SQLException exception = assertThrows(SQLException.class, () -> databaseManager.createUser(username, role, email));
        assertEquals("Username already exists: " + username, exception.getMessage());

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    public void updateUserByName_ExpectSuccess() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setRole(UserRole.VIEWER);
        updateRequest.setEmail("new_email@example.com");

        User updatedUser = databaseManager.updateUserByName(username, updateRequest);
        assertNotNull(updatedUser);
        assertEquals("new_email@example.com", updatedUser.getEmail());
        assertEquals(UserRole.VIEWER, updatedUser.getRole());

        User fetchedUser = databaseManager.getUserByName(username);
        assertNotNull(fetchedUser);
        assertEquals("new_email@example.com", fetchedUser.getEmail());
        assertEquals(UserRole.VIEWER, fetchedUser.getRole());
    }

    @Test
    public void updateUserById_ExpectSuccess() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        long userId = user.getId();

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setRole(UserRole.VIEWER);
        updateRequest.setEmail("new_email@example.com");

        User updatedUser = databaseManager.updateUserById(userId, updateRequest);
        assertNotNull(updatedUser);
        assertEquals("new_email@example.com", updatedUser.getEmail());
        assertEquals(UserRole.VIEWER, updatedUser.getRole());

        User fetchedUser = databaseManager.getUserById(userId);
        assertNotNull(fetchedUser);
        assertEquals("new_email@example.com", fetchedUser.getEmail());
        assertEquals(UserRole.VIEWER, fetchedUser.getRole());
    }

    @Test
    void deleteUserByName_ExpectSuccess()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should exist after creation");

        assertDoesNotThrow(() -> databaseManager.deleteUserByName(username));
        assertFalse(assertDoesNotThrow(() -> databaseManager.isUserExists(username)), "User should be deleted");
    }

    @Test
    void deleteUserById_ExpectSuccess()
    {
        Long userId = assertDoesNotThrow(() -> databaseManager.createUser(username, role, email).getId());
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(userId), "User should exist after creation"));

        assertDoesNotThrow(() -> databaseManager.deleteUserById(userId));
        assertFalse(assertDoesNotThrow(() -> databaseManager.isUserExists(userId)), "User should be deleted");
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

    @Test
    void getUserById_ExpectSuccess()
    {
        User user = assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(assertDoesNotThrow(() -> databaseManager.isUserExists(user.getId())), "User should exist after creation");

        assertNotNull(assertDoesNotThrow(() -> databaseManager.getUserByName(username)));
    }

    @Test
    void getUserById_ExpectFailed()
    {
        assertFalse(assertDoesNotThrow(() -> databaseManager.isUserExists(0L)), "User should no exist after creation");
        assertNull(assertDoesNotThrow(() -> databaseManager.getUserById(0L)));
    }

    @Test
    void isValidEmail_ExpectSuccess()
    {
        assertTrue(isValidEmail("loh_pidor@druzei.net"));
    }

    @Test
    void isValidEmail_ExpectFailed()
    {
        assertFalse(isValidEmail("loh_pidor"));
    }

    @Test
    void isValidRole_ExpectSuccess()
    {
        assertTrue(isValidRole("Admin"));
        assertTrue(isValidRole("User"));
        assertTrue(isValidRole("Viewer"));
    }

    @Test
    void isValidRole_ExpectFailed()
    {
        assertFalse(isValidRole("Pidor"));
    }
}