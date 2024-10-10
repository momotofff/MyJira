package swagger;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig
{
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("swagger.api")) // Укажите базовый пакет, где находятся ваши контроллеры
                .paths(PathSelectors.any())
                .build();
    }
}
