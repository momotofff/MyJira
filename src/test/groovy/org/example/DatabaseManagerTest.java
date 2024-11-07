package groovy.org.example;
import io.swagger.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseManagerTest
{
    private final String username = "testUser";
    private final String role = "User";
    private final String email = "testUser@example.com";

    private static final String USER = "postgresTest";
    private static final String PASS = "postgresTest";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test-db")
            .withUsername(USER)
            .withPassword(PASS)
            .withInitScript("init/init.sql");

    private static DatabaseManager databaseManager;

    @BeforeEach
    public void setUp()
    {
        String url = String.format("jdbc:postgresql://%s:%d/test-db", postgreSQLContainer.getHost(), postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
        databaseManager = new DatabaseManager(url, USER, PASS);
    }

    @Test
    @Order(1)
    void getConnectionTest()
    {
        try (Connection connection = databaseManager.getConnection())
        {
            assertNotNull(connection, "Соединение с базой данных не должно быть null");
            assertFalse(connection.isClosed(), "Соединение должно быть открыто");
        }
        catch (SQLException e)
        {
            fail("Не удалось получить соединение: " + e.getMessage());
        }
    }


    @Test
    @Order(2)
    public void CreateUserSuccessTest() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(role, user.getRole().toString());
        assertEquals(email, user.getEmail());

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    @Order(3)
    public void CreateUserDuplicateUsernameTest() throws SQLException
    {
        User user = databaseManager.createUser(username, role, email);
        assertNull(user);

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
        assertEquals(username, users.get(0).getUsername());
    }

    @Test
    @Order(4)
    public void GetUsersTest() throws SQLException
    {
        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
        assertEquals(username, users.get(0).getUsername());
    }

    @Test
    @Order(5)
    void deleteUserByUserName() throws SQLException
    {

        List<User> users = databaseManager.getUsers();
        assertEquals(1, users.size());
        assertEquals(username, users.get(0).getUsername());
        assertTrue(databaseManager.userExists(username), "Пользователь должен существовать перед удалением");
        databaseManager.deleteUserByUserName(username);
        assertFalse(databaseManager.userExists(username), "Пользователь должен быть удален");
    }
}