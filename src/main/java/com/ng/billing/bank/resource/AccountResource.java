package com.ng.billing.bank.resource;

import com.ng.billing.bank.application.CreateAccountCommand;
import com.ng.billing.bank.application.RetrieveAccountCommand;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
@Validated
@RequiredArgsConstructor
public class AccountResource {

    private final CreateAccountCommand createAccountCommand;
    private final RetrieveAccountCommand retrieveAccountCommand;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
        AccountResponseDto response = createAccountCommand.execute(createAccountRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public AccountResponseDto getAccount(@RequestParam(value = "numero_conta", required = true) String accountNumber) {
        return retrieveAccountCommand.execute(accountNumber);
    }

}
