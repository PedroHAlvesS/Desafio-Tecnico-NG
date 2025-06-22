package com.ng.billing.bank.domain.fee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebitCardFeeStrategyTest {

    @Nested
    @DisplayName("Given DebitCardFeeStrategy")
    class GivenDebitCardFeeStrategy {

        @Nested
        @DisplayName("When calculateTotalAmount is called")
        class WhenCalculateTotalAmountIsCalled {

            private BigDecimal result;

            @BeforeEach
            void setUp() {
                result = new DebitCardFeeStrategy().calculateTotalAmount(new BigDecimal("10.00"));
            }

            @Test
            @DisplayName("Then return amount with 3% fee")
            void thenReturnAmountWith3PercentFee() {
                assertEquals(new BigDecimal("10.30"), result);
            }
        }
    }
}
