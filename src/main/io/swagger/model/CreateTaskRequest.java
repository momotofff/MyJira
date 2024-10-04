package swagger.model;

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
 * CreateTaskRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")


public class CreateTaskRequest   {
  @JsonProperty("title")

  private String title = null;

  @JsonProperty("description")

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

  private PriorityEnum priority = null;

  @JsonProperty("author")

  private String author = null;

  @JsonProperty("assignee")

  private String assignee = null;


  public CreateTaskRequest title(String title) { 

    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   **/
  
  @Schema(example = "Новая задача", required = true, description = "")
  
  @NotNull
  public String getTitle() {  
    return title;
  }



  public void setTitle(String title) { 

    this.title = title;
  }

  public CreateTaskRequest description(String description) { 

    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  
  @Schema(example = "Описание новой задачи", required = true, description = "")
  
  @NotNull
  public String getDescription() {  
    return description;
  }



  public void setDescription(String description) { 

    this.description = description;
  }

  public CreateTaskRequest status(StatusEnum status) { 

    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  
  @Schema(example = "pending", required = true, description = "")
  
  @NotNull
  public StatusEnum getStatus() {  
    return status;
  }



  public void setStatus(StatusEnum status) { 

    this.status = status;
  }

  public CreateTaskRequest priority(PriorityEnum priority) { 

    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   **/
  
  @Schema(example = "medium", required = true, description = "")
  
  @NotNull
  public PriorityEnum getPriority() {  
    return priority;
  }



  public void setPriority(PriorityEnum priority) { 

    this.priority = priority;
  }

  public CreateTaskRequest author(String author) { 

    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   **/
  
  @Schema(example = "Автор", required = true, description = "")
  
  @NotNull
  public String getAuthor() {  
    return author;
  }



  public void setAuthor(String author) { 

    this.author = author;
  }

  public CreateTaskRequest assignee(String assignee) { 

    this.assignee = assignee;
    return this;
  }

  /**
   * Get assignee
   * @return assignee
   **/
  
  @Schema(example = "Исполнитель", required = true, description = "")
  
  @NotNull
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
    CreateTaskRequest createTaskRequest = (CreateTaskRequest) o;
    return Objects.equals(this.title, createTaskRequest.title) &&
        Objects.equals(this.description, createTaskRequest.description) &&
        Objects.equals(this.status, createTaskRequest.status) &&
        Objects.equals(this.priority, createTaskRequest.priority) &&
        Objects.equals(this.author, createTaskRequest.author) &&
        Objects.equals(this.assignee, createTaskRequest.assignee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, status, priority, author, assignee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateTaskRequest {\n");
    
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
