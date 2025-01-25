package momotoff.myjira.dbmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.Swagger2SpringBoot;
import io.swagger.api.TasksApiController;
import io.swagger.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Swagger2SpringBoot.class)
@AutoConfigureMockMvc

@ExtendWith(MockitoExtension.class)
public class TasksApiControllerTest extends DbManagerTestFixture
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

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        assertNotNull(Mockito.mock(Connection.class));

        assertNotNull(databaseManager.getConnection());
    }
}
