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
import java.sql.SQLException;
import java.util.List;

@RestController
@DependsOn("dbManager")
public class TasksApiController implements TasksApi
{
    private static final Logger logger = LoggerFactory.getLogger(TasksApiController.class);
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

    @Override
    public ResponseEntity<Task> createTask(@Parameter(in = ParameterIn.DEFAULT,
                                                      description = "",
                                                      required = true,
                                                      schema = @Schema())
                                         @Valid @RequestBody CreateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        long authorId;
        logger.info("Received request to create task by author: {}", author);

        try
        {
            authorId = databaseManager.getUserByName(author).getId();
        }
        catch (SQLException e)
        {
            logger.error("Failed to get user ID for author: {}", author, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                Task task = databaseManager.createTask(body.getTitle(), body.getDescription(), TaskStatus.PENDING.getValue(), TaskPriority.MEDIUM.getValue(), authorId);
                logger.info("Task created successfully with ID: {}", task.getId());
                return new ResponseEntity<>(task, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Failed to create task: {}", body, e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type received: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Task>> getAllTasks()
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get all tasks. Accept: {}", accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                List<Task> tasks = databaseManager.getTasks();
                logger.info("Successfully retrieved {} tasks.", tasks.size());
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for getAllTasks: {}", accept);
        return new ResponseEntity<List<Task>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Task> getTaskById(
            @Parameter(name = "taskId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("taskId") Long taskId
    )
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get task by ID: {}. Accept: {}", taskId, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                Task task = databaseManager.getTaskById(taskId);
                logger.info("Successfully retrieved task with ID: {}", taskId);
                return new ResponseEntity<>(task, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error retrieving task with ID: {}: {}", taskId, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for getTaskById: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Task> updateTaskById(
            @Parameter(name = "taskId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("taskId") Long taskId,
            @Parameter(name = "UpdateTaskRequest", description = "", required = true)
            @Valid @RequestBody UpdateTaskRequest body)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to update task with ID: {}. Accept: {}", taskId, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                Task updatedTask = databaseManager.updateTask(taskId, body.getTitle(), body.getDescription(), body.getStatus().toString(), body.getPriority().getValue(), taskId);
                logger.info("Successfully updated task with ID: {}", taskId);
                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error updating task with ID: {}: {}", taskId, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for updateTaskById: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> deleteTaskById(
            @Parameter(name = "taskId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("taskId") Long taskId
    )
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to delete task with ID: {}. Accept: {}", taskId, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                databaseManager.deleteTaskById(taskId);
                logger.info("Successfully deleted task with ID: {}", taskId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error deleting task with ID: " + taskId, e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for deleteTasksById: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    public ResponseEntity<List<Task>> getTasksByAssigneeId(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("userId") Long userId
    )
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get tasks by assignee ID: {}. Accept: {}", userId, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                List<Task> tasks = databaseManager.getTasksByAssigneeId(userId);
                logger.info("Successfully retrieved {} tasks for assignee ID: {}", tasks.size(), userId);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error retrieving tasks by assignee ID: {}: {}", userId, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for getTasksByAssigneeId: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Task>> getTasksByAuthorId(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH)
            @PathVariable("userId") Long userId
    )
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get tasks by author ID: {}. Accept: {}", userId, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                List<Task> tasks = databaseManager.getTasksByAuthorId(userId);
                logger.info("Successfully retrieved {} tasks for author ID: {}", tasks.size(), userId);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error retrieving tasks by author ID: {}: {}", userId, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for getTasksByAuthorId: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Task>> searchTasks(@NotNull @Parameter(in = ParameterIn.QUERY,
                                                                      description = "" ,
                                                                      required = true,
                                                                      schema = @Schema())
                                                  @Valid @RequestParam(value = "keyword", required = true) String keyword)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to search tasks with keyword: {}. Accept: {}", keyword, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                List<Task> tasks = databaseManager.searchTasks(keyword);
                logger.info("Successfully retrieved {} tasks for keyword: {}", tasks.size(), keyword);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Couldn't serialize response for keyword: {} content type application/json", keyword, e);
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for searchTasks: {}", accept);
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@Parameter(in = ParameterIn.PATH,
                                                                  description = "",
                                                                  required = true,
                                                                  schema = @Schema())
                                                       @PathVariable("status") String status)
    {
        String accept = request.getHeader("Accept");
        logger.info("Received request to get tasks by status: {}. Accept: {}", status, accept);

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                List<Task> tasks = databaseManager.getTasksByStatus(status);
                logger.info("Successfully retrieved {} tasks for status: {}", tasks.size(), status);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
            catch (SQLException e)
            {
                logger.error("Error retrieving tasks by status: {}: {}", status, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.warn("Unsupported media type for getTasksByStatus: {}", accept);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
