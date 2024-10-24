package io.swagger.api;

import groovy.org.example.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksApiController implements TasksApi
{
    private static final Logger log = LoggerFactory.getLogger(TasksApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    DatabaseManager databaseManager = new DatabaseManager();

    @Autowired
    public TasksApiController(ObjectMapper objectMapper, HttpServletRequest request)
    {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Task> createTask(@Parameter(in = ParameterIn.DEFAULT,
                                                      description = "",
                                                      required=true,
                                                      schema=@Schema())
                                           @Valid @RequestBody CreateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(objectMapper.readValue("{\n  \"author\" : \"Автор Задачи\",\n  \"description\" : \"Описание задачи 1\",\n  \"id\" : \"1\",\n  \"assignee\" : \"Исполнитель Задачи\",\n  \"title\" : \"Задача 1\",\n  \"priority\" : \"high\",\n  \"status\" : \"pending\"\n}", Task.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Task>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping
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

    public ResponseEntity<Void> deleteTaskById(@Parameter(in = ParameterIn.PATH,
                                                             description = "",
                                                             required=true,
                                                             schema=@Schema()) @PathVariable("taskId") String taskId)
    {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Task> getTaskById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema()) @PathVariable("taskId") String taskId
)   {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(objectMapper.readValue("{\n  \"author\" : \"Автор Задачи\",\n  \"description\" : \"Описание задачи 1\",\n  \"id\" : \"1\",\n  \"assignee\" : \"Исполнитель Задачи\",\n  \"title\" : \"Задача 1\",\n  \"priority\" : \"high\",\n  \"status\" : \"pending\"\n}", Task.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Task>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Task> putTaskById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                               @PathVariable("taskId") String taskId,
                                               @Parameter(in = ParameterIn.DEFAULT,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                               @Valid @RequestBody UpdateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(objectMapper.readValue("{\n  \"author\" : \"Автор Задачи\",\n  \"description\" : \"Описание задачи 1\",\n  \"id\" : \"1\",\n  \"assignee\" : \"Исполнитель Задачи\",\n  \"title\" : \"Задача 1\",\n  \"priority\" : \"high\",\n  \"status\" : \"pending\"\n}", Task.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Task>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Task> updateTaskById(@Parameter(in = ParameterIn.PATH,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                               @PathVariable("taskId") String taskId,
                                               @Parameter(in = ParameterIn.DEFAULT,
                                                          description = "",
                                                          required=true,
                                                          schema=@Schema())
                                               @Valid @RequestBody UpdateTaskRequest body)
    {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json"))
        {
            try
            {
                return new ResponseEntity<Task>(objectMapper.readValue("{\n  \"author\" : \"Автор Задачи\",\n  \"description\" : \"Описание задачи 1\",\n  \"id\" : 1,\n  \"assignee\" : \"Исполнитель Задачи\",\n  \"title\" : \"Задача 1\",\n  \"priority\" : \"high\",\n  \"status\" : \"pending\"\n}", Task.class), HttpStatus.NOT_IMPLEMENTED);
            }
            catch (IOException e)
            {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Task>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Task>(HttpStatus.NOT_IMPLEMENTED);
    }
}
