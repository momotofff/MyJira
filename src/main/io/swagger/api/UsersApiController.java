package io.swagger.api;

import io.swagger.model.*;
import momotoff.myjira.dbmanager.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;
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
public class UsersApiController implements UsersApi
{
    private static final Logger logger = LoggerFactory.getLogger(UsersApiController.class);
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

    @Override
    public ResponseEntity<List<User>> getAllUsers()
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get all users. Accept: {}", accept);

        if (accept == null)
        {
            logger.warn("Accept header is missing in the request.");
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
        }

        try
        {
            List<User> users = databaseManager.getUsers();

            if (users.isEmpty())
            {
                logger.info("No users found.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Successfully retrieved {} users.", users.size());
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (SQLException e)
        {
            logger.error("Error getting users list: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> postUser(@Parameter(in = ParameterIn.DEFAULT,
                                                    description = "",
                                                    required = true,
                                                    schema = @Schema())
                                         @Valid @RequestBody CreateUserRequest body)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to create user with username: {}. Accept: {}", body.getUsername(), accept);

        try
        {
            if (accept == null || !accept.contains("application/json"))
            {
                logger.warn("Invalid accept header: {}. Expected application/json.", accept);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            User createdUser = databaseManager.createUser(body.getUsername(), UserRole.USER.getValue(), body.getEmail());
            logger.info("Successfully created user with username: {}", body.getUsername());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        catch (SQLException e)
        {
            logger.error("Error creating user: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> getUserById(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("userId") Long userId)
    {
        logger.info("Received request to retrieve user by ID: {}", userId);
        User user = null;

        try
        {
            user = databaseManager.getUserById(userId);

            if (user != null)
            {
                logger.info("User found by ID: {}", userId);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else
            {
                logger.info("User not found by ID: {}", userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (SQLException e)
        {
            logger.error("Error retrieving user by ID: {}. {}", userId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> updateUserById(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("userId") Long userId,
            @Parameter(name = "UpdateUserRequest", description = "", required = true)
            @Valid
            @RequestBody UpdateUserRequest updateUserRequest)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to update user with ID: {}. Accept: {}", userId, accept);

        if (accept == null || !accept.contains("application/json"))
        {
            logger.warn("Invalid accept header for update user: {}. Expected application/json.", accept);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        User updatedUser = null;

        try
        {
            updatedUser = databaseManager.updateUserById(userId, updateUserRequest);
            logger.info("Successfully updated user with ID: {}", userId);
        }
        catch (SQLException e)
        {
            logger.error("Error updating user with ID: {}. {}", userId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (updatedUser == null)
        {
            logger.info("User with ID: {} not found.", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("userId") Long userId)
    {
        logger.info("Received request to delete user with ID: {}", userId);

        try
        {
            databaseManager.deleteUserById(userId);
            logger.info("Successfully deleted user with ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (SQLException e)
        {
            logger.error("Error deleting user with ID: {}. Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<User> getUserByName(
            @Parameter(in = ParameterIn.PATH, description = "Username of the user", required = true, schema = @Schema())
            @PathVariable("username") String username)
    {
        logger.info("Received request to retrieve user by username: {}", username);
        User user = null;

        try
        {
            user = databaseManager.getUserByName(username);

            if (user != null)
            {
                logger.info("User found by username: {}", username);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else
            {
                logger.info("User not found by username: {}", username);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (SQLException e)
        {
            logger.error("Error retrieving user by username: {}. {}", username, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> updateUserByName(
            @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
            @PathVariable("username") String username,
            @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema())
            @Valid
            @RequestBody UpdateUserRequest body)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to update user with username: {}. Accept: {}", username, accept);

        if (accept == null || !accept.contains("application/json"))
        {
            logger.warn("Invalid accept header for update user: {}. Expected application/json.", accept);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        User updatedUser = null;

        try
        {
            updatedUser = databaseManager.updateUserByName(username, body);
        }
        catch (SQLException e)
        {
            logger.error("Error updating user with username: {}. Error: {}", username, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (updatedUser == null)
        {
            logger.info("User with username: {} not found.", username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Successfully updated user with username: {}", username);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUserByName(
            @Parameter(in = ParameterIn.PATH, description = "Username of the user to delete", required = true, schema = @Schema())
            @PathVariable("username") String username)
    {
        logger.info("Received request to delete user by username: {}", username);

        try
        {
            databaseManager.deleteUserByName(username);
            logger.info("Successfully deleted user by username: {}", username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (SQLException e)
        {
            logger.error("Error deleting user by username: {}. {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}