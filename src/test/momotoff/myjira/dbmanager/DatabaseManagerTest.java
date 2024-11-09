package momotoff.myjira.dbmanager;

import io.swagger.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class DatabaseManagerTest
{
    private final String username = "testUser";
    private final String role = "User";
    private final String email = "testUser@example.com";

    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    private static final String DBNAME = "test-db";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName(DBNAME)
            .withUsername(USER)
            .withPassword(PASS)
            .withInitScript("init.sql");

    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void beforeAll()
    {
        String url = String.format("jdbc:postgresql://%s:%d/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                DBNAME
        );

        databaseManager = new DatabaseManager(url, USER, PASS);
    }

    @BeforeEach
    public void beforeEach()
    {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        containerDelegate.execute("DELETE FROM users", "", 0, true, true);
    }

    @Test
    void getConnection_ExpectFailed()
    {
        DatabaseManager failedDb = new DatabaseManager("For the Emperor!", USER, PASS);
        assertThrows(SQLException.class, failedDb::getConnection);
    }

    @Test
    void getConnection_ExpectSuccess()
    {
        Connection connection = assertDoesNotThrow(() -> databaseManager.getConnection());
        assertNotNull(connection);
        assertFalse(assertDoesNotThrow(connection::isClosed, "Connection should be open"));
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
        assertTrue(databaseManager.isUserExists(username), "User should exist after creation");

        assertDoesNotThrow(() -> databaseManager.deleteUserByUserName(username));
        assertFalse(databaseManager.isUserExists(username), "User should be deleted");
    }

    @Test
    void getUserByName_ExpectSuccess()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(databaseManager.isUserExists(username), "User should exist after creation");

        assertNotNull(assertDoesNotThrow(() -> databaseManager.getUserByName(username)));
    }

    @Test
    void getUserByName_ExpectFailed()
    {
        assertDoesNotThrow(() -> databaseManager.createUser(username, role, email));
        assertTrue(databaseManager.isUserExists(username), "User should exist after creation");

        assertNull(assertDoesNotThrow(() -> databaseManager.getUserByName("Emperor")));
    }
}