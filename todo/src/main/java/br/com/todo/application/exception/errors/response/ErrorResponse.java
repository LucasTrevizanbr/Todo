package br.com.todo.application.exception.errors.response;

import java.util.List;

public class ErrorResponse {

    private int httpCode;
    private String message;
    private String internalCode;
    private List<FieldErrorResponse> errors;

    public ErrorResponse(int httpCode, String message, String internalCode, List<FieldErrorResponse> errors) {
        this.httpCode = httpCode;
        this.message = message;
        this.internalCode = internalCode;
        this.errors = errors;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public List<FieldErrorResponse> getErrors() {
        return errors;
    }
}
