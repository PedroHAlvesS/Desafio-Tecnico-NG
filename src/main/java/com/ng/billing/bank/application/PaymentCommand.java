package com.ng.billing.bank.application;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.domain.mapper.AccountMapper;
import com.ng.billing.bank.infrastructure.h2.AccountRepository;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.PaymentRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentCommand {

    private final Logger logger = LoggerFactory.getLogger(PaymentCommand.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountResponseDto execute(PaymentRequestDto requestDto) {
        logger.info("[PaymentCommand:execute] Payment for account {}", requestDto.getAccountNumber());

        AccountEntity entity = accountRepository.findByAccountNumber(requestDto.getAccountNumber())
                .orElseThrow(() -> new GenericException(ErrosEnum.NOT_FOUND));

        BigDecimal totalAmountAfterFee = requestDto.getPaymentMethod().totalAmountAfterFee(requestDto.getAmount());

        if (entity.getAccountBalance().compareTo(totalAmountAfterFee) < 0) {
            logger.error("[PaymentCommand:execute] Payment for account {} with insufficient balance", requestDto.getAccountNumber());
            throw new GenericException(ErrosEnum.NOT_FOUND);
        }

        entity.setAccountBalance(entity.getAccountBalance().subtract(totalAmountAfterFee));
        accountRepository.save(entity);

        logger.info("[PaymentCommand:execute] Payment for account {} with success", requestDto.getAccountNumber());

        return accountMapper.accountEntityToAccountResponseDto(entity);
    }
}
