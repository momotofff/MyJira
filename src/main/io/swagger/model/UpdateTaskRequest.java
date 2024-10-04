package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UpdateTaskRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")


public class UpdateTaskRequest   {
  @JsonProperty("title")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String title = null;

  @JsonProperty("description")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String description = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    IN_PROGRESS("in_progress"),
    
    COMPLETED("completed");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("status")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private StatusEnum status = null;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    HIGH("high"),
    
    MEDIUM("medium"),
    
    LOW("low");

    private String value;

    PriorityEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PriorityEnum fromValue(String text) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("priority")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private PriorityEnum priority = null;

  @JsonProperty("author")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String author = null;

  @JsonProperty("assignee")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String assignee = null;


  public UpdateTaskRequest title(String title) { 

    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   **/
  
  @Schema(example = "Обновленное название задачи", description = "")
  
  public String getTitle() {  
    return title;
  }



  public void setTitle(String title) { 
    this.title = title;
  }

  public UpdateTaskRequest description(String description) { 

    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  
  @Schema(example = "Обновленное описание задачи", description = "")
  
  public String getDescription() {  
    return description;
  }



  public void setDescription(String description) { 
    this.description = description;
  }

  public UpdateTaskRequest status(StatusEnum status) { 

    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  
  @Schema(example = "in_progress", description = "")
  
  public StatusEnum getStatus() {  
    return status;
  }



  public void setStatus(StatusEnum status) { 
    this.status = status;
  }

  public UpdateTaskRequest priority(PriorityEnum priority) { 

    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   **/
  
  @Schema(example = "low", description = "")
  
  public PriorityEnum getPriority() {  
    return priority;
  }



  public void setPriority(PriorityEnum priority) { 
    this.priority = priority;
  }

  public UpdateTaskRequest author(String author) { 

    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   **/
  
  @Schema(example = "Обновленный автор", description = "")
  
  public String getAuthor() {  
    return author;
  }



  public void setAuthor(String author) { 
    this.author = author;
  }

  public UpdateTaskRequest assignee(String assignee) { 

    this.assignee = assignee;
    return this;
  }

  /**
   * Get assignee
   * @return assignee
   **/
  
  @Schema(example = "Обновленный исполнитель", description = "")
  
  public String getAssignee() {  
    return assignee;
  }



  public void setAssignee(String assignee) { 
    this.assignee = assignee;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateTaskRequest updateTaskRequest = (UpdateTaskRequest) o;
    return Objects.equals(this.title, updateTaskRequest.title) &&
        Objects.equals(this.description, updateTaskRequest.description) &&
        Objects.equals(this.status, updateTaskRequest.status) &&
        Objects.equals(this.priority, updateTaskRequest.priority) &&
        Objects.equals(this.author, updateTaskRequest.author) &&
        Objects.equals(this.assignee, updateTaskRequest.assignee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, status, priority, author, assignee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateTaskRequest {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    assignee: ").append(toIndentedString(assignee)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
