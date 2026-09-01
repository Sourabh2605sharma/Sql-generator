package com.learn.sql_ai_generator.util;

public class SqlResponseCleaner {

    public static String clean(String response) {

        if (response == null) {
            return "";
        }

        return response
                .replace("```sql", "")
                .replace("```", "")
                .replace("\\n", System.lineSeparator())
                .replace("\r", "")
                .trim();
    }
}