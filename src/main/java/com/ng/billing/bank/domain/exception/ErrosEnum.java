package com.ng.billing.bank.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrosEnum {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Requisição inválida"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Recurso não encontrado"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Erro interno do servidor"),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "Recurso em conflito");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
