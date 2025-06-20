package com.ng.billing.bank.domain.exception.generic;

import com.ng.billing.bank.domain.exception.ErrosEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericExceptionDto {

    private String code;
    private String message;

    public GenericExceptionDto(ErrosEnum errosEnum) {
        this.code = errosEnum.getCode();
        this.message = errosEnum.getMessage();
    }
}
