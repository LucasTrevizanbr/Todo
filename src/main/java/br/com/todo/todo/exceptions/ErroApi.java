package br.com.todo.todo.exceptions;

import br.com.todo.todo.dto.ErroValidacaoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroApi {


    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroValidacaoDto> handler(MethodArgumentNotValidException exception) {

        List<ErroValidacaoDto> errosDto = new ArrayList<>();
        List<FieldError> errosCampos = exception.getBindingResult().getFieldErrors();

        errosCampos.stream().forEach(erroCampoX -> {
            String mensagemErro = messageSource.getMessage(erroCampoX, LocaleContextHolder.getLocale());
            errosDto.add(new ErroValidacaoDto(erroCampoX.getField(), mensagemErro));
        });
        return errosDto;
    }

}
