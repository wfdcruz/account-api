package com.ebanx.accountapi.controller;

import com.ebanx.accountapi.controller.request.AccountRequest;
import com.ebanx.accountapi.controller.response.AccountResponse;
import com.ebanx.accountapi.entity.Account;
import com.ebanx.accountapi.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestScope
@Slf4j
public class AccountController {

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/reset")
    public String resetAccounts() {
        service.reset();
        return "OK";
    }

    @GetMapping(value = "/balance", produces = "application/json")
    public ResponseEntity<BigDecimal> getAccount(@RequestParam String account_id) {
        try {
            return ResponseEntity.ok(service.getBalance(account_id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
        }
    }

    @PostMapping(value = "/event", produces = "application/json")
    public ResponseEntity<?> execute(@RequestBody AccountRequest request) {
        AccountResponse.AccountResponseBuilder acc = AccountResponse.builder();

        try {
            switch (request.getType()) {
                case deposit:
                    acc.destination(service.deposit(request.getDestination(), request.getAmount()));
                    break;
                case withdraw:
                    acc.origin(service.withdraw(request.getOrigin(), request.getAmount()));
                    break;
                case transfer:
                    Map<String, Account> accounts = service.transfer(request);
                    acc.origin(accounts.get("origin"));
                    acc.destination(accounts.get("destination"));
                    break;
                default:
                    throw new RuntimeException("Event not supported.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(acc.build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
        }
    }

}
