package io.swagger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController
{
    @GetMapping("/greet")
    public String greet() {
        return "Добро пожаловать пацак! Два раза \"КУ\" будешь делать ";
    }
}
