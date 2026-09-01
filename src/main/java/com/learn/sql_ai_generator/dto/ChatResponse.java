package com.learn.sql_ai_generator.dto;

public class ChatResponse {

    private String sql;
    private String message;

    public ChatResponse() {
    }

    public ChatResponse(String sql, String message) {
        this.sql = sql;
        this.message = message;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}