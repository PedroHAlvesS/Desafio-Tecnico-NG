package com.ng.billing.bank.domain.fee;

import java.math.BigDecimal;

public interface FeeStrategy {
    BigDecimal calculateTotalAmount(BigDecimal amount);
}
