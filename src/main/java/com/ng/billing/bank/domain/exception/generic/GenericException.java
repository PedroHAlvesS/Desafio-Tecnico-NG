package com.ng.billing.bank.domain.exception.generic;

import com.ng.billing.bank.domain.exception.ErrosEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class GenericException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public GenericException(ErrosEnum errosEnum) {
        this.httpStatus = errosEnum.getHttpStatus();
        this.code = errosEnum.getCode();
        this.message = errosEnum.getMessage();
    }
}
