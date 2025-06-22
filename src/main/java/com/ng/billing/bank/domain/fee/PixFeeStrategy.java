package com.ng.billing.bank.domain.fee;

import java.math.BigDecimal;

public class PixFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount) {
        return amount;
    }
}
