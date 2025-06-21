package com.ng.billing.bank.resource.dto;

import com.ng.billing.bank.domain.exception.generic.GenericException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PaymentMethodTypeEnumTest {

    @Nested
    @DisplayName("Given PaymentMethodTypeEnum")
    class GivenPaymentMethodTypeEnum {
        @Nested
        @DisplayName("When fromDescription is called")
        class WhenFromDescriptionIsCalled {
            @Nested
            @DisplayName("And description is valid")
            class AndDescriptionIsValid {
                @Test
                @DisplayName("Then return PaymentMethodTypeEnum PIX")
                void thenReturnPaymentMethodTypeEnumPix() {
                    assertEquals(PaymentMethodTypeEnum.PIX, PaymentMethodTypeEnum.fromDescription("P"));
                }

                @Test
                @DisplayName("Then return PaymentMethodTypeEnum DEBIT_CARD")
                void thenReturnPaymentMethodTypeEnumDebitCard() {
                    assertEquals(PaymentMethodTypeEnum.DEBIT_CARD, PaymentMethodTypeEnum.fromDescription("D"));
                }

                @Test
                @DisplayName("Then return PaymentMethodTypeEnum CREDIT_CARD")
                void thenReturnPaymentMethodTypeEnumCreditCard() {
                    assertEquals(PaymentMethodTypeEnum.CREDIT_CARD, PaymentMethodTypeEnum.fromDescription("C"));
                }
            }

            @Nested
            @DisplayName("And description is invalid")
            class AndDescriptionIsInvalid {
                @Test
                @DisplayName("Then throw GenericException")
                void thenThrowGenericException() {
                    assertThrows(GenericException.class, () -> PaymentMethodTypeEnum.fromDescription("X"));
                }
            }
        }

        @Nested
        @DisplayName("When totalAmountAfterFee is called")
        class WhenTotalAmountAfterFeeIsCalled {

            @Nested
            @DisplayName("And paymentMethodTypeEnum is PIX")
            class AndPaymentMethodTypeEnumIsPix {
                @Test
                @DisplayName("Then return amount")
                void thenReturnAmount() {
                    BigDecimal amount = new BigDecimal("10.00");
                    assertEquals(new BigDecimal("10.00"), PaymentMethodTypeEnum.PIX.totalAmountAfterFee(amount));
                }
            }

            @Nested
            @DisplayName("And paymentMethodTypeEnum is DEBIT_CARD")
            class AndPaymentMethodTypeEnumIsDebitCard {
                @Test
                @DisplayName("Then return amount")
                void thenReturnAmount() {
                    BigDecimal amount = new BigDecimal("10.00");
                    assertEquals(new BigDecimal("10.30"), PaymentMethodTypeEnum.DEBIT_CARD.totalAmountAfterFee(amount));
                }
            }

            @Nested
            @DisplayName("And paymentMethodTypeEnum is CREDIT_CARD")
            class AndPaymentMethodTypeEnumIsCreditCard {
                @Test
                @DisplayName("Then return amount")
                void thenReturnAmount() {
                    BigDecimal amount = new BigDecimal("10.00");
                    assertEquals(new BigDecimal("10.50"), PaymentMethodTypeEnum.CREDIT_CARD.totalAmountAfterFee(amount));
                }
            }
        }

        @Nested
        @DisplayName("When getDescription is called")
        class WhenGetDescriptionIsCalled {
            @Test
            @DisplayName("Then return description")
            void thenReturnDescription() {
                assertEquals("P", PaymentMethodTypeEnum.PIX.getDescription());
                assertEquals("D", PaymentMethodTypeEnum.DEBIT_CARD.getDescription());
                assertEquals("C", PaymentMethodTypeEnum.CREDIT_CARD.getDescription());
            }
        }
    }
}