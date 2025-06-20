package com.ng.billing.bank.domain.mapper;

import com.ng.billing.bank.domain.entities.AccountEntity;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountBalance", source = "accountBalance")
    AccountEntity accountRequestToAccountEntity(CreateAccountRequestDto requestDto);

    @Mapping(target = "accountBalance", source = "accountBalance")
    AccountResponseDto accountEntityToAccountResponseDto(AccountEntity accountEntity);


}
