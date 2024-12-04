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

    @PutMapping("/users/{username}")
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

    @PutMapping("/{id}")
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

    @GetMapping("/name/{username}")
    public ResponseEntity<User> getUserByName(@Parameter(in = ParameterIn.PATH,
                                                         description = "Username of the user",
                                                         required = true,
                                                         schema = @Schema())
                                              @PathVariable("username") String username)
    {
        User user = null;

        try
        {
            user = databaseManager.getUserByName(username);

            if (user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (SQLException e)
        {
            log.error("Error retrieving user by username", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/id/{userId}")
    public ResponseEntity<User> getUserById(@Parameter(in = ParameterIn.PATH,
                                                       description = "ID of the user",
                                                       required = true,
                                                       schema = @Schema())
                                            @PathVariable("userId") long userId)
    {
        User user = null;

        try
        {
            user = databaseManager.getUserById(userId);

            if (user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch (SQLException e)
        {
            log.error("Error retrieving user by ID", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/name/{username}")
    public ResponseEntity<Void> deleteUserByName(@Parameter(in = ParameterIn.PATH,
                                                            description = "Username of the user to delete",
                                                            required = true,
                                                            schema = @Schema())
                                                 @PathVariable("username") String username)
    {
        try
        {
            if (databaseManager.isUserExists(username))
            {
                databaseManager.deleteUserByName(username);
                return ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        catch (SQLException e)
        {
            log.error("Error deleting user by username", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                                 @PathVariable("userId") Long userId,
                                                 @RequestHeader(required = false) String accept)
    {
        try
        {
            if (databaseManager.isUserExists(userId))
            {
                databaseManager.deleteUserById(userId);
                return ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}