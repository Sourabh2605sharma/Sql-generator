package com.learn.sql_ai_generator.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class UserPromptValidator {

    private UserPromptValidator() {
    }

    private static final Pattern FORBIDDEN_OPERATIONS = Pattern.compile(
            "\\b(insert|update|delete|drop|truncate|alter|create|" +
                    "grant|revoke|merge|replace|upsert)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern MODIFICATION_INTENT = Pattern.compile(
            "\\b(change|modify|remove|rename|edit|add|set)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern RETRIEVAL_WORDS = Pattern.compile(
            "\\b(select|show|display|list|get|find|fetch|retrieve|" +
                    "view|see|count|number|records|rows|data|employee|" +
                    "employees|user|users|customer|customers|order|orders|" +
                    "product|products)\\b",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean isSelectRequest(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            return false;
        }

        String normalized = prompt
                .trim()
                .toLowerCase(Locale.ROOT);

        /*
         * Reject SQL modification operations.
         */
        if (FORBIDDEN_OPERATIONS.matcher(normalized).find()) {
            return false;
        }

        /*
         * Reject modification intent.
         */
        if (MODIFICATION_INTENT.matcher(normalized).find()) {
            return false;
        }

        /*
         * Require some indication that the user wants
         * to retrieve information.
         */
        return RETRIEVAL_WORDS.matcher(normalized).find();
    }
}