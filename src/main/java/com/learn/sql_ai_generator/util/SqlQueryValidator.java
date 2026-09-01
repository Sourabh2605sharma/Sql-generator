package com.learn.sql_ai_generator.util;

import java.util.Locale;

public class SqlQueryValidator {

    private SqlQueryValidator() {
    }

    public static boolean isSelectQuery(String sql) {

        if (sql == null || sql.isBlank()) {
            return false;
        }

        String normalized = sql
                .trim()
                .toLowerCase(Locale.ROOT);

        // Remove trailing semicolon
        if (normalized.endsWith(";")) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }

        // Must start with SELECT
        if (!normalized.startsWith("select ")) {
            return false;
        }

        // Extra protection against multiple statements
        if (normalized.contains(";")) {
            return false;
        }

        // Explicitly reject dangerous SQL operations
        String[] forbiddenKeywords = {
                "insert ",
                "update ",
                "delete ",
                "drop ",
                "truncate ",
                "alter ",
                "create ",
                "grant ",
                "revoke ",
                "merge "
        };

        for (String keyword : forbiddenKeywords) {
            if (normalized.contains(keyword)) {
                return false;
            }
        }

        return true;
    }
}