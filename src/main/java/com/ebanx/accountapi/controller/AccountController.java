package com.ebanx.accountapi.controller;

import com.ebanx.accountapi.controller.request.AccountRequest;
import com.ebanx.accountapi.controller.response.AccountResponse;
import com.ebanx.accountapi.entity.Account;
import com.ebanx.accountapi.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public void resetAccounts() {
        service.reset();
    }

    @GetMapping(value = "/balance/account_id={accountId}", produces = "application/json")
    public BigDecimal getAccount(@PathVariable Long accountId) {
        try {
            return service.getBalance(accountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist", e);
        }
    }

    @PostMapping(value = "/event", produces = "application/json")
    public AccountResponse execute(@RequestBody AccountRequest request) {
        try {
            Account serviceResponse = service.processRequest(request);
            return AccountResponse
                    .builder()
                    .destination(serviceResponse)
                    .build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist", e);
        }
    }

}
