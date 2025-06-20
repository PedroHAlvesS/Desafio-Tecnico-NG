package com.ng.billing.bank.config;

import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.exception.generic.GenericExceptionDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    private final MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<GenericExceptionDto>> handleInvalidArgument(MethodArgumentNotValidException exception) {
        logger.error("[ApplicationExceptionHandler:handleInvalidArgument] Error in request", exception);
        List<GenericExceptionDto> responseDTOList = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            GenericExceptionDto error = new GenericExceptionDto(e.getField(), message);
            responseDTOList.add(error);
        });
        return ResponseEntity.badRequest().body(responseDTOList);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericExceptionDto> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        logger.error("[ApplicationExceptionHandler:handleDataIntegrityViolation] Error in request", exception);
        GenericExceptionDto error = new GenericExceptionDto(ErrosEnum.CONFLICT);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericExceptionDto> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        logger.error("[ApplicationExceptionHandler:handleHttpMessageNotReadable] Error in request", exception);
        GenericExceptionDto error = new GenericExceptionDto(ErrosEnum.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericExceptionDto> genericHandler(Exception exception) {
        logger.error("[ApplicationExceptionHandler:genericHandler] Error in request", exception);
        if (exception instanceof GenericException parsedException) {
            return ResponseEntity.status(parsedException.getHttpStatus()).body(new GenericExceptionDto(parsedException.getCode(), parsedException.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
