package com.ng.billing.bank.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateAccountRequestDto {

    @NotBlank(message = "O campo numero_conta é obrigatório")
    @JsonProperty("numero_conta")
    private String accountNumber;

    @JsonProperty("saldo")
    @PositiveOrZero
    private BigDecimal accountBalance;
}
