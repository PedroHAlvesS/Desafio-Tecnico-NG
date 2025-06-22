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
class PixFeeStrategyTest {

    @Nested
    @DisplayName("Given PixFeeStrategy")
    class GivenPixFeeStrategy {

        @Nested
        @DisplayName("When calculateTotalAmount is called")
        class WhenCalculateTotalAmountIsCalled {
            private BigDecimal result;

            @BeforeEach
            void setUp() {
                result = new PixFeeStrategy().calculateTotalAmount(new BigDecimal("10.00"));
            }

            @Test
            @DisplayName("Then should return amount without fee")
            void thenShouldReturnAmount() {
                assertEquals(new BigDecimal("10.00"), result);
            }
        }
    }
}
