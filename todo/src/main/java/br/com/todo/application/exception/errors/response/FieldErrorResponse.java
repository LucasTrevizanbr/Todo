package br.com.todo.application.exception.errors.response;

public class FieldErrorResponse {

    private String message;
    private String error;

    public FieldErrorResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}

