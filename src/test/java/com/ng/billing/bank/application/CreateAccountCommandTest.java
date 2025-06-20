package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
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
class CreateAccountCommandTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private CreateAccountCommand createAccountCommand;

    @Nested
    @DisplayName("Given CreateAccountCommand")
    class GivenCreateAccountCommand {

        @Nested
        @DisplayName("When execute with success")
        class WhenExecuteWithSuccess {
            private AccountResponseDto expectedResponse;
            private AccountResponseDto result;

            @BeforeEach
            void setUp() {
                expectedResponse = new AccountResponseDto("accountNumber", BigDecimal.ONE);
                AccountEntity accountEntity = new AccountEntity(1L, "accountNumber", BigDecimal.ONE);

                when(accountMapper.accountRequestToAccountEntity(new CreateAccountRequestDto("accountNumber", BigDecimal.ONE)))
                        .thenReturn(accountEntity);

                when(accountMapper.accountEntityToAccountResponseDto(accountEntity))
                        .thenReturn(expectedResponse);

                when(accountRepository.save(accountEntity)).thenReturn(accountEntity);

                when(accountRepository.findByAccountNumber("accountNumber")).thenReturn(Optional.empty());

                result = createAccountCommand.execute(new CreateAccountRequestDto("accountNumber", BigDecimal.ONE));
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

            Exception exception;

            @BeforeEach
            void setUp() {
                when(accountMapper.accountRequestToAccountEntity(new CreateAccountRequestDto("accountNumber", BigDecimal.ONE)))
                        .thenReturn(new AccountEntity());

                when(accountRepository.findByAccountNumber("accountNumber"))
                        .thenReturn(Optional.of(new AccountEntity()));

                exception = assertThrows(GenericException.class,
                        () -> createAccountCommand.execute(new CreateAccountRequestDto("accountNumber", BigDecimal.ONE))
                );
            }

            @Test
            @DisplayName("Then should throw GenericException of CONFLICT")
            void thenShouldThrowGenericExceptionOfCONFLICT() {
                assertEquals(ErrosEnum.CONFLICT.getMessage(), exception.getMessage());
            }
        }
    }
}