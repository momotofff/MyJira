package momotoff.myjira.dbmanager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
public abstract class DbManagerTestFixture
{
    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    private static final String DBNAME = "test-dbUsers";

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName(DBNAME)
            .withUsername(USER)
            .withPassword(PASS)
            .withInitScript("init.sql");

    protected static DatabaseManager databaseManager;

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