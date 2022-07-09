package br.com.todo.application.exception.errors;

public class UnfinishedTasksException extends RuntimeException {


    private final String message;
    private final String internalCode;

    public UnfinishedTasksException(String message, String internalCode) {
        this.message = message;
        this.internalCode = internalCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getInternalCode() {
        return internalCode;
    }
}
