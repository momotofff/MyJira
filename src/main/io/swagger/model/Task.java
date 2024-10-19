package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")


public class Task   {
  @JsonProperty("id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer id = null;

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
  public enum StatusEnum
  {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    private String value;

    StatusEnum(String value) { this.value = value;}

    @Override
    @JsonValue
    public String toString() { return String.valueOf(value);}

    @JsonCreator
    public static StatusEnum fromValue(String text)
    {
      for (StatusEnum b : StatusEnum.values())
        if (String.valueOf(b.value).equals(text))
          return b;

      return null;
    }
  }

  @JsonProperty("status")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private StatusEnum status = null;

  //Gets or Sets priority

  public enum PriorityEnum
  {
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
    public static PriorityEnum fromValue(String text)
    {
      for (PriorityEnum b : PriorityEnum.values())
        if (String.valueOf(b.value).equals(text))
          return b;

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


  public Task id(Integer id)
  {
    this.id = id;
    return this;
  }

  //Get id @return id

  @Schema(example = "1", description = "")
  
  public Integer getId() { return id;}

  public void setId(Integer id) { this.id = id;}

  public Task title(String title)
  {
    this.title = title;
    return this;
  }


  //Get title @return title
  
  @Schema(example = "Задача 1", description = "")
  
  public String getTitle() { return title;}

  public void setTitle(String title) { this.title = title;}

  public Task description(String description)
  {
    this.description = description;
    return this;
  }

 //Get description @return description

  
  @Schema(example = "Описание задачи 1", description = "")
  
  public String getDescription() { return description;}

  public void setDescription(String description) { this.description = description;}

  public Task status(StatusEnum status)
  {
    this.status = status;
    return this;
  }

  //Get status @return status
  
  @Schema(example = "pending", description = "")
  
  public StatusEnum getStatus() { return status;}

  public void setStatus(StatusEnum status) { this.status = status;}

  public Task priority(PriorityEnum priority)
  {
    this.priority = priority;
    return this;
  }

  //Get priority @return priority
  
  @Schema(example = "high", description = "")
  public PriorityEnum getPriority() { return priority;}

  public void setPriority(PriorityEnum priority) { this.priority = priority;}

  public Task author(String author)
  {
    this.author = author;
    return this;
  }

  //Get author @return author

  
  @Schema(example = "Автор Задачи", description = "")
  public String getAuthor() { return author;}

  public void setAuthor(String author) { this.author = author;}

  public Task assignee(String assignee)
  {
    this.assignee = assignee;
    return this;
  }

  //Get assignee @return assignee
  
  @Schema(example = "Исполнитель Задачи", description = "")
  
  public String getAssignee() { return assignee;}

  public void setAssignee(String assignee) { this.assignee = assignee;}

  @Override
  public boolean equals(java.lang.Object o)
  {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;

    Task task = (Task) o;

    return Objects.equals(this.id, task.id) &&
        Objects.equals(this.title, task.title) &&
        Objects.equals(this.description, task.description) &&
        Objects.equals(this.status, task.status) &&
        Objects.equals(this.priority, task.priority) &&
        Objects.equals(this.author, task.author) &&
        Objects.equals(this.assignee, task.assignee);
  }

  @Override
  public int hashCode() { return Objects.hash(id, title, description, status, priority, author, assignee);}

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Task {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    assignee: ").append(toIndentedString(assignee)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  //Convert the given object to string with each line indented by 4 spaces (except the first line).

  private String toIndentedString(java.lang.Object o)
  {
    if (o == null)
      return "null";

    return o.toString().replace("\n", "\n    ");
  }
}
