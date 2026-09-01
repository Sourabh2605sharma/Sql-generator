package com.learn.sql_ai_generator.util;

import java.util.Locale;

public class UserPromptValidator {

    private UserPromptValidator() {
    }

    public static boolean isSelectRequest(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            return false;
        }

        String normalized = prompt.toLowerCase(Locale.ROOT);

        String[] forbiddenWords = {
                "update",
                "change",
                "modify",
                "set",
                "alter",
                "edit",
                "rename",
                "delete",
                "remove",
                "insert",
                "add",
                "create",
                "drop",
                "truncate",
                "replace",
                "upsert"
        };

        for (String word : forbiddenWords) {

            if (normalized.matches(".*\\b" + word + "\\b.*")) {
                return false;
            }
        }

        return true;
    }
}