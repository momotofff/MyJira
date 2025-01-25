package momotoff.myjira.dbmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

public class UsersApiControllerTest extends DbManagerTestFixture
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp()
    {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        containerDelegate.execute("DELETE FROM tasks", "", 0, true, true);
        containerDelegate.execute("DELETE FROM users", "", 0, true, true);
    }

}
