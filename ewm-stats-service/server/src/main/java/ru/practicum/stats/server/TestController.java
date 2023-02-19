package ru.practicum.stats.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    String test() {
        return("Тест stats-сервис");
    }
}
