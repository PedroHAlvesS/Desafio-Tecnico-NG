package com.ng.billing.bank.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    @JsonProperty("numero_conta")
    @NotBlank
    private String accountNumber;

    @JsonProperty("valor")
    @PositiveOrZero
    private BigDecimal amount;

    @JsonProperty("forma_pagamento")
    @Schema(implementation = PaymentMethodTypeEnum.class)
    private PaymentMethodTypeEnum paymentMethod;
}
