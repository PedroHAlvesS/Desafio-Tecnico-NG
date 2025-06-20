package com.ng.billing.bank.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    @JsonProperty("numero_conta")
    private String accountNumber;

    @JsonProperty("saldo")
    private BigDecimal accountBalance;
}
