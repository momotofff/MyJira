/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.62).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package swagger.api;

import swagger.model.CreateTaskRequest;
import swagger.model.Error;
import swagger.model.Task;
import swagger.model.UpdateTaskRequest;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")
@Validated
public interface TasksApi
{
    @Operation(summary = "Task creation method", description = "", tags={ "Task" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Successful task creation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
        
        @ApiResponse(responseCode = "200", description = "Дефолтный ответ") })
    @RequestMapping(value = "/tasks",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Task> createTask(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateTaskRequest body
);

    @Operation( summary = "Method for getting a list of tasks",
                description = "The method is intended to obtain a list of all tasks saved in the database.",
                tags={ "Task" })

    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                            description = "List of tasks",
                            content = @Content( mediaType = "application/json",
                                                array = @ArraySchema(schema = @Schema(implementation = Task.class)))),
                            @ApiResponse(responseCode = "501",
                            description = "Method not implemented in code",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))),
                            @ApiResponse(responseCode = "200",
                            description = "Дефолтный ответ") })

    @RequestMapping(value = "/tasks",
                    produces = { "application/json" },
                    method = RequestMethod.GET)

    ResponseEntity<List<Task>> getAllTasks();


    @Operation( summary = "Удалить задачу",
                description = "",
                tags={ "Task" })

    @ApiResponses(value = { @ApiResponse(responseCode = "204",
                            description = "Задача удалена"),
                            @ApiResponse(responseCode = "404",
                            description = "Задача не найдена"),
                            @ApiResponse(responseCode = "200",
                            description = "Дефолтный ответ") })

    @RequestMapping(value = "/tasks/{taskId}",
                    method = RequestMethod.DELETE)

    ResponseEntity<Void> tasksTaskIdDelete(@Parameter(in = ParameterIn.PATH,
                                                      description = "",
                                                      required=true,
                                                      schema=@Schema())
                                           @PathVariable("taskId") String taskId);


    @Operation( summary = "Get task by ID",
                description = "",
                tags={ "Task" })

    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                            description = "Задача найдена",
                            content = @Content(mediaType = "application/json",
                                               schema = @Schema(implementation = Task.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Задача не найдена"),
                            @ApiResponse(responseCode = "200",
                                         description = "Дефолтный ответ") })

    @RequestMapping(value = "/tasks/{taskId}",
                    produces = { "application/json" },
                    method = RequestMethod.GET)

    ResponseEntity<Task> tasksTaskIdGet(@Parameter(in = ParameterIn.PATH,
                                                   description = "",
                                                   required=true,
                                                   schema=@Schema())
                                        @PathVariable("taskId") String taskId);

    @Operation( summary = "Обновить задачу",
                description = "", tags={ "Task" })

    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                                         description = "Задача обновлена",
                                         content = @Content(mediaType = "application/json",
                                                            schema = @Schema(implementation = Task.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Задача не найдена"),
                            @ApiResponse(responseCode = "200",
                                         description = "Дефолтный ответ") })

    @RequestMapping(value = "/tasks/{taskId}",
                    produces = { "application/json" },
                    consumes = { "application/json" },
                    method = RequestMethod.PUT)

    ResponseEntity<Task> tasksTaskIdPut(@Parameter(in = ParameterIn.PATH,
                                                   description = "",
                                                   required=true,
                                                   schema=@Schema()) @PathVariable("taskId") String taskId,
                                        @Parameter(in = ParameterIn.DEFAULT,
                                                   description = "",
                                                   required=true,
                                                   schema=@Schema())
                                        @Valid @RequestBody UpdateTaskRequest body);

}

