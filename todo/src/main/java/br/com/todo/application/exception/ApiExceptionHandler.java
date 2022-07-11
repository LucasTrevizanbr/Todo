package br.com.todo.application.exception;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.application.exception.errors.StoppedGoalException;
import br.com.todo.application.exception.errors.UnfinishedTasksException;
import br.com.todo.application.exception.errors.response.ErrorResponse;
import br.com.todo.application.exception.errors.response.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex ,
                                                                               WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ApiError.TG002.getMessageError(),
                ApiError.TG002.name(),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(errorField -> new FieldErrorResponse(
                                errorField.getDefaultMessage(),
                                errorField.getField()
                        ))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getInternalCode(),
                null
        );
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnfinishedTasksException.class)
    public ResponseEntity<ErrorResponse> handleUnfinishedTasksException(UnfinishedTasksException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ex.getInternalCode(),
                null
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(StoppedGoalException.class)
    public ResponseEntity<ErrorResponse> handleStoppedGoalException(StoppedGoalException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ex.getInternalCode(),
                null
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
