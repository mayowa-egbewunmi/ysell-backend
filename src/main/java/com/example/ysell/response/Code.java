package com.example.ysell.response;

public class Code {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Code{" +
                "message='" + message + '\'' +
                '}';
    }
}
