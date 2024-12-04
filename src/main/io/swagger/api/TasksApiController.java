package io.swagger.api;

import io.swagger.model.TaskPriority;
import io.swagger.model.TaskStatus;
import momotoff.myjira.dbmanager.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.model.CreateTaskRequest;
import io.swagger.model.Task;
import io.swagger.model.UpdateTaskRequest;
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
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")

@RestController
@DependsOn("dbManager")
@RequestMapping("/tasks")
public class TasksApiController implements TasksApi
{
    private static final Logger log = LoggerFactory.getLogger(TasksApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    public TasksApiController(ObjectMapper objectMapper, HttpServletRequest request)
    {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> postTask(@Parameter(in = ParameterIn.DEFAULT,
                                                      description = "",
                                                      required = true,
                                                      schema = @Schema())
                                         @Valid @RequestBody CreateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        long authorId;

        try
        {
            authorId = databaseManager.getUserByName(author).getId();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(databaseManager.createTask(body.getTitle(), body.getDescription(), TaskStatus.PENDING.getValue(), TaskPriority.MEDIUM.getValue(), authorId), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks()
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity(databaseManager.getTasks(), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> getTaskById(@Parameter(in = ParameterIn.PATH,
                                                       description = "",
                                                       required = true,
                                                       schema = @Schema())
                                                @PathVariable("taskId") long taskId
    )
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(databaseManager.getTaskById(taskId), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTaskById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required = true,
                                                          schema = @Schema())
                                               @PathVariable("taskId") long taskId,
                                               @Parameter(in = ParameterIn.DEFAULT,
                                                          description = "",
                                                          required = true,
                                                          schema = @Schema())
                                               @Valid @RequestBody UpdateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(databaseManager.updateTask(taskId, body.getTitle(), body.getDescription(), body.getStatus().toString(), body.getPriority().getValue(), taskId), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTasksById(@Parameter(in = ParameterIn.PATH,
                                                                description = "",
                                                                required = true,
                                                                schema = @Schema())
                                                @PathVariable("taskId") long taskId) throws SQLException
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                databaseManager.deleteTaskById(taskId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            catch (SQLException e)
            {
                log.error("Error deleting task with ID: " + taskId, e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }


    @GetMapping("/tasks/by-username/{assigneeName}")
    public ResponseEntity<List<Task>> getTasksByAssigneeName(@Parameter(in = ParameterIn.PATH,
                                                                        description = "The username of the user whose tasks are to be retrieved.",
                                                                        required = true,
                                                                        schema = @Schema())
                                                            @PathVariable("assigneeName") String assigneeName)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try {
                return new ResponseEntity<List<Task>>(databaseManager.getTasksByAssigneeName(assigneeName), HttpStatus.OK);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/by-id/{assigneeId}")
    public ResponseEntity<List<Task>> getTasksByAssigneeId(@Parameter(in = ParameterIn.PATH,
                                                                      description = "The ID of the user whose tasks are to be retrieved.",
                                                                      required = true,
                                                                      schema = @Schema())
                                                           @PathVariable("userId") long userId)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<List<Task>>(databaseManager.getTasksByAssigneeId(userId), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/by-username/author/{authorName}")
    public ResponseEntity<List<Task>> getTasksByAuthorName(@Parameter(in = ParameterIn.PATH,
                                                                      description = "The username of the user whose tasks are to be retrieved.",
                                                                      required = true,
                                                                      schema = @Schema())
                                                           @PathVariable("authorName") String authorName)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try {
                return new ResponseEntity<List<Task>>(databaseManager.getTasksByAuthorName(authorName), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/by-id/author/{authorId}")
    public ResponseEntity<List<Task>> getTasksByAuthorId(@Parameter(in = ParameterIn.PATH,
                                                                    description = "The ID of the user whose tasks are to be retrieved.",
                                                                    required = true,
                                                                    schema = @Schema())
                                                         @PathVariable("authorId") long authorId)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<List<Task>>(databaseManager.getTasksByAuthorId(authorId), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/search/{keyword}")
    public ResponseEntity<List<Task>> searchTasks(@NotNull @Parameter(in = ParameterIn.QUERY,
                                                                      description = "" ,
                                                                      required = true,
                                                                      schema = @Schema())
                                                  @Valid @RequestParam(value = "keyword",
                                                                       required = true) String keyword)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<List<Task>>(databaseManager.searchTasks(keyword), HttpStatus.OK);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/tasks/by-status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@Parameter(in = ParameterIn.PATH,
                                                                  description = "",
                                                                  required = true,
                                                                  schema = @Schema())
                                                       @PathVariable("status") String status)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<List<Task>>(databaseManager.getTasksByStatus(status), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }
}
