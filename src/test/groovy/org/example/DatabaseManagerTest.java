package groovy.org.example;

import io.swagger.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseManagerTest
{
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @Container
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test-db")
            .withUsername(USER)
            .withPassword(PASS)
            .withInitScript("init.sql");

    private DatabaseManager databaseManager;

    private final String username = "testUser";
    private final String role = "User";
    private final String email = "testUser@example.com";
    int countUsers;

    @BeforeEach
    public void setUp() throws SQLException
    {
        postgres.start();
        String url = String.format("jdbc:postgresql://%s:%d/postgres", postgres.getHost(), postgres.getExposedPorts().get(0));
        databaseManager = new DatabaseManager(url, "postgres", "postgres");
        countUsers = databaseManager.getUsers().size();
    }

    @Test
    @Order(1)
    void getConnectionTest()
    {
        assertDoesNotThrow(() -> {
            Connection connection = databaseManager.getConnection();

            assertNotNull(connection, "Соединение с базой данных не должно быть null");
            assertFalse(connection.isClosed(), "Соединение должно быть открыто");
        });
    }

    @Test
    @Order(2)
    public void CreateUserSuccessTest()
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(role, user.getRole().toString());
        assertEquals(email, user.getEmail());
    }

    @Test
    @Order(3)
    public void CreateUserDuplicateUsernameTest()
    {
        User user = databaseManager.createUser(username, role, email);
        assertNull(user);
    }

    @Test
    @Order(4)
    public void GetUsersTest() throws SQLException
    {
        List<User> users = databaseManager.getUsers();
        assertEquals(countUsers, users.size());
        assertEquals(username, users.get(countUsers - 1).getUsername());
    }

    @Test
    @Order(5)
    void deleteUserByUserName()
    {
        assertTrue(databaseManager.userExists(username));
        databaseManager.deleteUserByUserName(username);
        assertFalse(databaseManager.userExists(username));
    }
}