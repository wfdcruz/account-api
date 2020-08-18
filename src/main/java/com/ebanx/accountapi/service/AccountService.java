package com.ebanx.accountapi.service;

import com.ebanx.accountapi.controller.request.AccountRequest;
import com.ebanx.accountapi.entity.Account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AccountService {
    private final List<Account> accounts = new ArrayList<>();

    public Account createAccount(Long accountId, BigDecimal amount) {
        log.info("Creating an account with id: " + accountId);
        Account acc = Account.builder()
                .id(accountId)
                .balance(amount)
                .build();

        this.accounts.add(acc);
        return getAccount(acc.getId());
    }

    public Account getAccount(Long id) throws NoSuchElementException {
        log.info("Finding accountId: " + id);
        return accounts.stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Account not found");
                    throw new NoSuchElementException();
                });
    }

    public BigDecimal getBalance(Long accountId){
        return getAccount(accountId).getBalance();
    }

    public void reset(){
        log.info("Clearing accounts...");
        this.accounts.clear();
    }

    public Account processRequest(AccountRequest request) throws NoSuchElementException {
        switch (request.getType()) {
            case deposit:
                return createAccount(request.getDestination(), request.getAmount());
            case withdraw:
            case transfer:
            default:
                throw new RuntimeException("Not implemented");
        }
    }
}
