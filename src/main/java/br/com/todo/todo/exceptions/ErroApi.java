package br.com.todo.todo.exceptions;

import br.com.todo.todo.exceptions.dtoexception.ErroGenericoDto;
import br.com.todo.todo.exceptions.dtoexception.ErroValidacaoDto;
import br.com.todo.todo.dto.TarefaDtoDetalhado;
import br.com.todo.todo.exceptions.dtoexception.TarefasInacabadasDto;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TarefasInacabadasException.class)
    public TarefasInacabadasDto handlerTarefasInacabadas(TarefasInacabadasException exception) {
        List<TarefaDtoDetalhado> tarefasNaoConcluidas = exception.getTarefasNaoConcluidas();
        TarefasInacabadasDto dtoErro = new TarefasInacabadasDto(tarefasNaoConcluidas);
        return dtoErro;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TarefaNaoPresenteNaMetaException.class)
    public ErroGenericoDto handlerTarefaNaoPresente(TarefaNaoPresenteNaMetaException exception) {
        ErroGenericoDto erroDto = new ErroGenericoDto(exception.getMensagem());
        return erroDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MetaParadaException.class)
    public ErroGenericoDto handlerMetaParadaException(MetaParadaException exception) {
        ErroGenericoDto erroDto = new ErroGenericoDto(exception.getMensagem());
        return erroDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SenhaInvalidaException.class)
    public ErroGenericoDto handlerMetaParadaException(SenhaInvalidaException exception) {
        ErroGenericoDto erroDto = new ErroGenericoDto(exception.getMensagem());
        return erroDto;
    }


}
