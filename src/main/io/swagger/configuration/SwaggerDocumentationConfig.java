package swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-03T12:07:30.310865165Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig
{
    @Bean
    public OpenAPI openApi()
    {
        return new OpenAPI()
                .info(new Info()
                .title("Task management system")
                .description("This is a sample Task management system based on the OpenAPI 3.0 specification.")
                .termsOfService("")
                .version("0.0.1")
                .license(new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                .email("")));
    }
}
