package com.ng.billing.bank.resource.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Getter
public enum PaymentMethodTypeEnum {
    PIX("P", new BigDecimal("1.00")),
    DEBIT_CARD("D", new BigDecimal("1.03")),
    CREDIT_CARD("C", new BigDecimal("1.05"));

    private final String description;

    private final BigDecimal fee;

    public BigDecimal totalAmountAfterFee(BigDecimal amount) {
        return amount.multiply(fee).setScale(2, RoundingMode.HALF_EVEN);
    }

    @JsonCreator
    public static PaymentMethodTypeEnum fromDescription(String description) {
        for (PaymentMethodTypeEnum paymentMethodTypeEnum : PaymentMethodTypeEnum.values()) {
            if (paymentMethodTypeEnum.getDescription().equals(description)) {
                return paymentMethodTypeEnum;
            }
        }
        throw new GenericException(ErrosEnum.BAD_REQUEST);
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
