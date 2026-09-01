package com.learn.sql_ai_generator.controller;

import com.learn.sql_ai_generator.dto.ChatResponse;
import com.learn.sql_ai_generator.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(
            value = "/generate",
            consumes = "text/plain",
            produces = "application/json"
    )
    public ChatResponse generateSql(@RequestBody String prompt) {
        return aiService.chat(prompt);
    }
}