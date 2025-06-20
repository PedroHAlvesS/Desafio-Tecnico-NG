package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAccountCommandTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private RetrieveAccountCommand retrieveAccountCommand;

    @Nested
    @DisplayName("Given RetrieveAccountCommand")
    class GivenRetrieveAccountCommand {
        @Nested
        @DisplayName("When execute with success")
        class WhenExecuteWithSuccess {
            private AccountResponseDto result;
            private AccountResponseDto expectedResponse;

            @BeforeEach
            void setUp() {
                expectedResponse = new AccountResponseDto("accountNumber", BigDecimal.ONE);
                AccountEntity accountEntity = new AccountEntity(1L, "accountNumber", BigDecimal.ONE);
                when(accountRepository.findByAccountNumber("accountNumber"))
                        .thenReturn(Optional.of(accountEntity));

                when(accountMapper.accountEntityToAccountResponseDto(accountEntity))
                        .thenReturn(expectedResponse);

                result = retrieveAccountCommand.execute("accountNumber");
            }

            @Test
            @DisplayName("Then return accountResponseDto")
            void thenReturnAccountResponseDto() {
                assertEquals(expectedResponse, result);
            }
        }

        @Nested
        @DisplayName("When execute with failure")
        class WhenExecuteWithFailure {
            private Exception exception;

            @BeforeEach
            void setUp() {
                when(accountRepository.findByAccountNumber("accountNumber"))
                        .thenReturn(Optional.empty());

                exception = assertThrows(GenericException.class,
                        () -> retrieveAccountCommand.execute("accountNumber")
                );
            }

            @Test
            @DisplayName("Then should throw GenericException of NOT_FOUND")
            void thenShouldThrowGenericExceptionOfNOT_FOUND() {
                assertEquals(ErrosEnum.NOT_FOUND.getMessage(), exception.getMessage());
            }
        }
    }
}