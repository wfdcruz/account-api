package com.ebanx.accountapi.controller;

import com.ebanx.accountapi.controller.request.AccountRequest;
import com.ebanx.accountapi.controller.response.AccountResponse;
import com.ebanx.accountapi.entity.Account;
import com.ebanx.accountapi.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    @GetMapping(value = "/balance",  produces = "application/json")
    public ResponseEntity<BigDecimal> getAccount(@RequestParam String account_id) {
        try {
            return ResponseEntity.ok(service.getBalance(account_id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
        }
    }

    @PostMapping(value = "/event", produces = "application/json")
    public ResponseEntity<AccountResponse> execute(@RequestBody AccountRequest request) {
        try {
            Account serviceResponse = service.processRequest(request);
            AccountResponse acc = AccountResponse
                    .builder()
                    .destination(serviceResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(acc);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist", e);
        }
    }

}
