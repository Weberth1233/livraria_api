package com.weberth.libraryapi.exceptions;

public class OperacaoNaoPermitidaException extends  RuntimeException{
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
