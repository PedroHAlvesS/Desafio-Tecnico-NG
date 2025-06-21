package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.PaymentMethodTypeEnum;
import com.ng.billing.bank.resource.dto.PaymentRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCommandTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private PaymentCommand paymentCommand;

    @Nested
    @DisplayName("Given PaymentCommand")
    class GivenPaymentCommand {
        @Nested
        @DisplayName("When execute with Success")
        class WhenExecuteWithSuccess {

            private AccountResponseDto expectedResponse;
            private AccountResponseDto result;
            private AccountEntity accountEntity;

            @BeforeEach
            void setUp() {
                expectedResponse = new AccountResponseDto("123", new BigDecimal("90.00"));
                accountEntity = new AccountEntity(1L, "123", new BigDecimal("100.00"));
                when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(accountEntity));
                when(accountMapper.accountEntityToAccountResponseDto(accountEntity)).thenReturn(expectedResponse);

                result = paymentCommand.execute(new PaymentRequestDto("123", new BigDecimal("10.00"), PaymentMethodTypeEnum.PIX));
            }
            @Test
            @DisplayName("Then return AccountResponseDto")
            void thenReturnAccountResponseDto() {
                assertEquals(expectedResponse, result);
            }

            @Test
            @DisplayName("Then should save account")
            void thenCallAccountRepositorySave() {
                verify(accountRepository).save(accountEntity);
            }
        }

        @Nested
        @DisplayName("When account not found")
        class WhenAccountNotFound {

            private Exception exception;

            @BeforeEach
            void setUp() {
                PaymentRequestDto paymentRequestDto = new PaymentRequestDto("123", new BigDecimal("10.00"), PaymentMethodTypeEnum.PIX);
                when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.empty());
                exception = assertThrows(GenericException.class, () -> paymentCommand.execute(paymentRequestDto));
            }
            @Test
            @DisplayName("Then throw Not Found Exception")
            void thenThrowNotFoundExcept() {
                assertEquals(ErrosEnum.NOT_FOUND.getMessage(), exception.getMessage());
            }
        }

        @Nested
        @DisplayName("When account dont have enough balance")
        class WhenAccountDontHaveEnoughBalance {
            private Exception exception;

            @BeforeEach
            void setUp() {
                PaymentRequestDto paymentRequestDto = new PaymentRequestDto("123", new BigDecimal("96.00"), PaymentMethodTypeEnum.CREDIT_CARD);
                AccountEntity accountEntity = new AccountEntity(1L, "123", new BigDecimal("100.00"));
                when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(accountEntity));
                exception = assertThrows(GenericException.class, () -> paymentCommand.execute(paymentRequestDto));
            }
            @Test
            @DisplayName("Then throw Not Found Exception")
            void thenThrowNotFoundExcept() {
                assertEquals(ErrosEnum.NOT_FOUND.getMessage(), exception.getMessage());
            }
        }
    }
}