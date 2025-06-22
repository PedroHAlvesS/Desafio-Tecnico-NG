package com.ng.billing.bank.domain.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DebitCardFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount) {
        return amount.multiply(new BigDecimal("1.03")).setScale(2, RoundingMode.HALF_EVEN);
    }
}
