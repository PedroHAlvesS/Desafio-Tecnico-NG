package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RetrieveAccountCommand {

    private final Logger logger = LoggerFactory.getLogger(RetrieveAccountCommand.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountResponseDto execute(String accountNumber) {
        logger.info("[AccountCommand:execute] retrieving account {}", accountNumber);

        AccountEntity entity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new GenericException(ErrosEnum.NOT_FOUND));

        logger.info("[AccountCommand:execute] Account retrieved with success");

        return accountMapper.accountEntityToAccountResponseDto(entity);
    }
}


