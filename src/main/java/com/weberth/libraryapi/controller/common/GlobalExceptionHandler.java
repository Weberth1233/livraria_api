package com.weberth.libraryapi.controller.common;

import com.weberth.libraryapi.controller.dto.ErroCampo;
import com.weberth.libraryapi.controller.dto.ErrorResposta;
import com.weberth.libraryapi.exceptions.CampoInvalidoException;
import com.weberth.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResposta hendleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors.
                stream().map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage())).toList();
        return new ErrorResposta(HttpStatus.UNPROCESSABLE_CONTENT.value(), "Erro de validação!", listaErros);

    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResposta handleRegistroDuplicadoException(RegistroDuplicadoException e) {
        return ErrorResposta.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        return ErrorResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResposta handleCampoInvalidoException(CampoInvalidoException e){
        return new ErrorResposta
                (HttpStatus.UNPROCESSABLE_CONTENT.value(),
                        "Erro de validação!",
                        List.of(new ErroCampo(e.getCampo(), e.getMessage())));

    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResposta handleErrosNaoTratados(RuntimeException e){
        System.out.println(e.getMessage());
        System.out.println(e);
        return new ErrorResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocorreu um erro inesperado. Entre em contato com a administração",List.of());
    }

}
