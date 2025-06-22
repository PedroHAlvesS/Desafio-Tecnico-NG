package com.ng.billing.bank.domain.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreditCardFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount) {
        return amount.multiply(new BigDecimal("1.05")).setScale(2, RoundingMode.HALF_EVEN);
    }
}
