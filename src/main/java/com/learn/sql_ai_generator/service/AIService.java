package com.learn.sql_ai_generator.service;

import com.learn.sql_ai_generator.entity.Prompt;
import com.learn.sql_ai_generator.repository.PromptRepository;
import com.learn.util.SqlResponseCleaner;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AIService {

    private final ChatModel chatModel;
    private final PromptRepository promptRepository;

    private static final String SYSTEM_PROMPT = """
            You are an expert PostgreSQL SQL Generator.

            Rules:
            1. Return ONLY valid executable PostgreSQL SQL.
            2. Do NOT explain the SQL.
            3. Do NOT use markdown.
            4. Do NOT use ```sql or ```.
            5. Do NOT add comments.
            6. Do NOT add headings.
            7. Do NOT add English text.
            8. Output must contain only executable SQL.
            9. If multiple SQL statements are required, separate them using semicolons.
            10. Ensure all foreign keys reference PRIMARY KEY or UNIQUE columns.
            11. SQL should execute successfully in PostgreSQL without modification.
            """;

    public AIService(ChatModel chatModel,
                     PromptRepository promptRepository) {
        this.chatModel = chatModel;
        this.promptRepository = promptRepository;
    }

    public String chat(String userMessage) {

        // Save only the user's request
        Prompt prompt = new Prompt();
        prompt.setPrompt(userMessage);
        prompt.setInsertDateTime(LocalDateTime.now());
        promptRepository.save(prompt);

        // Combine the fixed system prompt with the user's request
        String finalPrompt = SYSTEM_PROMPT
                + "\n\nUser Request:\n"
                + userMessage;

        // Call the LLM
        String sql = chatModel.chat(finalPrompt);

        // Clean the response
        sql = SqlResponseCleaner.clean(sql);

        System.out.println("Generated SQL:");
        System.out.println(sql);

        return sql;
    }
}