package com.ng.billing.bank.resource.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.fee.CreditCardFeeStrategy;
import com.ng.billing.bank.domain.fee.DebitCardFeeStrategy;
import com.ng.billing.bank.domain.fee.FeeStrategy;
import com.ng.billing.bank.domain.fee.PixFeeStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Getter
public enum PaymentMethodTypeEnum {
    PIX("P", new PixFeeStrategy()),
    DEBIT_CARD("D", new DebitCardFeeStrategy()),
    CREDIT_CARD("C", new CreditCardFeeStrategy());

    private final String description;

    private final FeeStrategy feeStrategy;

    public BigDecimal totalAmountAfterFee(BigDecimal amount) {
        return feeStrategy.calculateTotalAmount(amount);
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
