package com.learn.sql_ai_generator.service;

import com.learn.sql_ai_generator.dto.ChatResponse;
import com.learn.sql_ai_generator.util.SqlQueryValidator;
import com.learn.sql_ai_generator.util.SqlResponseCleaner;
import com.learn.sql_ai_generator.util.UserPromptValidator;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatModel chatModel;

    public AIService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public ChatResponse chat(String userPrompt) {

        /*
         * ---------------------------------------------------------
         * STEP 1: Validate user input
         * ---------------------------------------------------------
         *
         * We do not allow requests asking for:
         *
         * INSERT
         * UPDATE
         * DELETE
         * DROP
         * TRUNCATE
         * ALTER
         * CREATE
         * GRANT
         * REVOKE
         * MERGE
         *
         * Such requests are rejected before calling the LLM.
         */

        if (!UserPromptValidator.isSelectRequest(userPrompt)) {

            return new ChatResponse(
                    null,
                    "Only SELECT queries are allowed. " +
                            "INSERT, UPDATE, DELETE, DROP, TRUNCATE, " +
                            "ALTER and other data-modifying operations " +
                            "are not permitted."
            );
        }


        /*
         * ---------------------------------------------------------
         * STEP 2: Validate empty request
         * ---------------------------------------------------------
         */

        if (userPrompt == null || userPrompt.isBlank()) {

            return new ChatResponse(
                    null,
                    "Please provide a request for a SELECT query."
            );
        }


        /*
         * ---------------------------------------------------------
         * STEP 3: Build our INTERNAL prompt
         * ---------------------------------------------------------
         *
         * The API user does NOT need to provide this prompt.
         *
         * The user only sends something like:
         *
         * "show all employees"
         *
         * Our application adds these instructions automatically.
         */

        String message = """
                You are a PostgreSQL SELECT query generator.

                YOUR JOB:
                Convert the user's natural language request into
                a valid PostgreSQL SELECT query.

                STRICT RULES:

                1. Generate SELECT queries ONLY.

                2. NEVER generate INSERT queries.

                3. NEVER generate UPDATE queries.

                4. NEVER generate DELETE queries.

                5. NEVER generate DROP queries.

                6. NEVER generate TRUNCATE queries.

                7. NEVER generate ALTER queries.

                8. NEVER generate CREATE queries.

                9. NEVER generate GRANT queries.

                10. NEVER generate REVOKE queries.

                11. NEVER generate MERGE queries.

                12. NEVER modify database data.

                13. NEVER modify database structure.

                14. Return ONLY the SQL query.

                15. Do NOT return markdown.

                16. Do NOT use ```sql.

                17. Do NOT provide explanations.

                18. The final answer MUST be a single SELECT statement.

                USER REQUEST:
                %s
                """.formatted(userPrompt);


        /*
         * ---------------------------------------------------------
         * STEP 4: Call LangChain4j / OpenRouter / Qwen
         * ---------------------------------------------------------
         */

        String sql;

        try {

            sql = chatModel.chat(message);

        } catch (Exception e) {

            return new ChatResponse(
                    null,
                    "Unable to generate SQL query. " +
                            "Please try again later."
            );
        }


        /*
         * ---------------------------------------------------------
         * STEP 5: Clean the AI response
         * ---------------------------------------------------------
         *
         * Your existing SqlResponseCleaner is kept here.
         */

        sql = SqlResponseCleaner.clean(sql);


        /*
         * ---------------------------------------------------------
         * STEP 6: VERY IMPORTANT
         *
         * Validate the AI-generated SQL.
         *
         * Even though we told Qwen to generate SELECT only,
         * we NEVER blindly trust the LLM response.
         * ---------------------------------------------------------
         */

        if (!SqlQueryValidator.isSelectQuery(sql)) {

            System.out.println(
                    "REJECTED NON-SELECT SQL FROM AI: " + sql
            );

            return new ChatResponse(
                    null,
                    "The generated query was rejected because " +
                            "only SELECT queries are allowed."
            );
        }


        /*
         * ---------------------------------------------------------
         * STEP 7: Return successful response
         * ---------------------------------------------------------
         */

        System.out.println("GENERATED SELECT SQL: " + sql);

        return new ChatResponse(
                sql,
                "SELECT query generated successfully."
        );
    }
}