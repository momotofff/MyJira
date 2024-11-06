package io.swagger;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateTimeConverter;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.*;
import com.fasterxml.jackson.databind.Module;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = {"io.swagger", "io.swagger.api" , "io.swagger.configuration", "io.swagger.model"})

public class Swagger2SpringBoot implements CommandLineRunner
{
    public static void main(String[] args) throws Exception
    {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    @Override
    public void run(String... arg0) throws Exception
    {
        if (arg0.length > 0 && arg0[0].equals("exitcode"))
            throw new ExitException();
    }

    @Bean
    public Module jsonNullableModule() { return new JsonNullableModule();}

    @Configuration
    static class CustomDateConfig extends WebMvcConfigurerAdapter
    {
        @Override
        public void addFormatters(FormatterRegistry registry)
        {
            registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
            registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator
    {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode()
        {
            return 10;
        }
    }
}
