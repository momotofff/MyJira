package io.swagger.api;

import io.swagger.model.UpdateUserRequest;
import momotoff.myjira.dbmanager.DatabaseManager;
import io.swagger.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;
import io.swagger.model.CreateUserRequest;
import io.swagger.model.User;
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
@DependsOn("dbManager")
@RequestMapping("/users")
public class UsersApiController implements UsersApi
{
    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request)
    {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers()
    {
        String accept = request.getHeader("Accept");

        if (accept == null)
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);

        try
        {
            List<User> users = databaseManager.getUsers();

            if (users.isEmpty())
                return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        }
        catch (SQLException e)
        {
            log.error("Error getting users list: {}", e.getMessage(), e);

            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> postUser(@Parameter(in = ParameterIn.DEFAULT,
                                                     description = "",
                                                     required = true,
                                                     schema = @Schema())
                                          @Valid @RequestBody CreateUserRequest body)
    {
        String accept = request.getHeader("Accept");

        try
        {
            if (accept == null || !accept.contains("application/json"))
                return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<User>(databaseManager.createUser(body.getUsername(), UserRole.USER.getValue(), body.getEmail()), HttpStatus.CREATED);
        }
        catch (SQLException e)
        {
            log.error("Error getting users list: {}", e.getMessage(), e);
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/{username}")
    public ResponseEntity<User> updateUserByName(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required = true,
                                                          schema = @Schema())
                                               @PathVariable("username") String username,
                                               @Parameter(in = ParameterIn.DEFAULT,
                                                          description = "",
                                                          required = true,
                                                          schema = @Schema())
                                               @Valid @RequestBody UpdateUserRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        User updatedUser = null;
        try
        {
            updatedUser = databaseManager.updateUserByName(username, body);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        if (updatedUser == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<User> updateUserById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required = true,
                                                          schema = @Schema())
                                                 @PathVariable("id") long userId,
                                                 @Parameter(in = ParameterIn.DEFAULT,
                                                         description = "",
                                                         required = true,
                                                         schema = @Schema())
                                                 @Valid @RequestBody UpdateUserRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        User updatedUser = null;
        try
        {
            updatedUser = databaseManager.updateUserById(userId, body);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        if (updatedUser == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByName(@Parameter(in = ParameterIn.PATH,
                                                       description = "",
                                                       required = true,
                                                       schema = @Schema())
                                            @PathVariable("username") String username)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"role\" : \"User\",\n  \"id\" : 1,\n  \"email\" : \"user@example.com\",\n  \"username\" : \"username\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/users/name/{username}")
    public ResponseEntity<Void> deleteUserByName(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                                 @PathVariable("username") String username,
                                                 @RequestHeader(required = false) String accept) throws SQLException
    {
        if (databaseManager.isUserExists(username))
        {
            databaseManager.deleteUserByUserName(username);
            return ResponseEntity.noContent().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/id/{userId}")
    public ResponseEntity<Void> deleteUserById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                                 @PathVariable("userId") Long userId,
                                                 @RequestHeader(required = false) String accept) throws SQLException
    {
        if (databaseManager.isUserExists(userId))
        {
            databaseManager.deleteUserByUserId(userId);
            return ResponseEntity.noContent().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}