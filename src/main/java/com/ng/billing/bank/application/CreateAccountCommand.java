package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateAccountCommand {

    private final Logger logger = LoggerFactory.getLogger(CreateAccountCommand.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountResponseDto execute(CreateAccountRequestDto createAccountRequestDto) {
        logger.info("[AccountCommand:execute] Creating account {}", createAccountRequestDto);

        AccountEntity entity = accountMapper.accountRequestToAccountEntity(createAccountRequestDto);

        accountRepository.findByAccountNumber(createAccountRequestDto.getAccountNumber()).ifPresent(account -> {
            logger.error("[AccountCommand:execute] Account already exists {}", account.getAccountNumber());
            throw new GenericException(ErrosEnum.CONFLICT);
        });

        accountRepository.save(entity);

        logger.info("[AccountCommand:execute] Account created with success");

        return accountMapper.accountEntityToAccountResponseDto(entity);
    }
}
