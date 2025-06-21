package com.ng.billing.bank.resource;

import com.ng.billing.bank.application.CreateAccountCommand;
import com.ng.billing.bank.application.RetrieveAccountCommand;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "conta")
public class AccountResource {

    private final CreateAccountCommand createAccountCommand;
    private final RetrieveAccountCommand retrieveAccountCommand;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova conta")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
        AccountResponseDto response = createAccountCommand.execute(createAccountRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Recupera uma conta pelo número da conta")
    public ResponseEntity<AccountResponseDto> getAccount(@RequestParam(value = "numero_conta", required = true) String accountNumber) {
        AccountResponseDto response = retrieveAccountCommand.execute(accountNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
