package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import io.swagger.configuration.NotUndefined;
import javax.validation.constraints.*;

@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")


public class CreateUserRequest
{
  @JsonProperty("username")

  private String username = null;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    ADMIN("admin"),
    
    EDITOR("editor"),
    
    VIEWER("viewer");

    private String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String text) {
      for (RoleEnum b : RoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("role")

  private RoleEnum role = null;

  @JsonProperty("email")

  private String email = null;


  public CreateUserRequest username(String username) { 

    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   **/
  
  @Schema(example = "username", required = true, description = "")
  
  @NotNull
  public String getUsername() {  
    return username;
  }



  public void setUsername(String username) { 

    this.username = username;
  }

  public CreateUserRequest role(RoleEnum role) { 

    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   **/
  
  @Schema(example = "editor", required = true, description = "")
  
  @NotNull
  public RoleEnum getRole() {  
    return role;
  }



  public void setRole(RoleEnum role) { 

    this.role = role;
  }

  public CreateUserRequest email(String email) { 

    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  
  @Schema(example = "user@example.com", required = true, description = "")
  
  @NotNull
  public String getEmail() {  
    return email;
  }



  public void setEmail(String email) { 

    this.email = email;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUserRequest createUserRequest = (CreateUserRequest) o;
    return Objects.equals(this.username, createUserRequest.username) &&
        Objects.equals(this.role, createUserRequest.role) &&
        Objects.equals(this.email, createUserRequest.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, role, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUserRequest {\n");
    
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
