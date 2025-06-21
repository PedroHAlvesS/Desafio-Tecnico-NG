package com.ng.billing.bank.resource;

import com.ng.billing.bank.application.PaymentCommand;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.PaymentRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
@Validated
@RequiredArgsConstructor
public class PaymentResource {

    private final PaymentCommand paymentCommand;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<AccountResponseDto> payment(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        AccountResponseDto responseDto = paymentCommand.execute(paymentRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
