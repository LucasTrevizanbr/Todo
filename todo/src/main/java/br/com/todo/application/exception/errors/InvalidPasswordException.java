package br.com.todo.application.exception.errors;

public class InvalidPasswordException extends RuntimeException{

    private final String message;
    private final String internalCode;

    public InvalidPasswordException(String message, String errorCode){
        this.message = message;
        this.internalCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getInternalCode() {
        return internalCode;
    }
}
