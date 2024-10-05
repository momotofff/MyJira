package swagger.api;

import org.example.DatabaseManager;
import org.springframework.web.bind.annotation.*;
import swagger.model.CreateUserRequest;
import swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")

@RestController
@RequestMapping("/users")
public class UsersApiController implements UsersApi
{
    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    DatabaseManager databaseManager = new DatabaseManager();

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request)
    {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<User>> usersGet()
    {
        String accept = request.getHeader("Accept");

        if (accept == null)
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);

        try
        {
            return new ResponseEntity(databaseManager.getUsers(), HttpStatus.OK);
        }
        catch (SQLException e)
        {
            System.err.println("Error getting users list: " + e.getMessage());
            // TODO: Return verbose error info instead of simple 500
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<User> usersPost(@Parameter(in = ParameterIn.DEFAULT,
                                                     description = "",
                                                     required=true,
                                                     schema=@Schema())
                                          @Valid @RequestBody CreateUserRequest body)
    {
        // TODO: Implement
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"role\" : \"editor\",\n  \"id\" : 1,\n  \"email\" : \"user@example.com\",\n  \"username\" : \"username\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }
}