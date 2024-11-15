package momotoff.myjira.dbmanager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
public class TasksDbManagerTest
{
    private final String authorName = "testUser";
    private final String authorRole = "User";
    private final String authorEmail = "testUser@example.com";

    private final String username = "testUser";
    private final String role = "User";
    private final String email = "testUser@example.com";


    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    private static final String DBNAME = "test-dbTasks";

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
        containerDelegate.execute("DELETE FROM tasks", "", 0, true, true);
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




}
