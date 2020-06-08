package com.example.ysell.model;

public class Password {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Password{" +
                "message='" + message + '\'' +
                '}';
    }
}
