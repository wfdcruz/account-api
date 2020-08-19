package com.ebanx.accountapi.service;

import com.ebanx.accountapi.controller.request.AccountRequest;
import com.ebanx.accountapi.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class AccountService {
    private final List<Account> accounts = new ArrayList<>();

    public Account withdraw(String originId, BigDecimal amount) throws NoSuchElementException {
        Account acc = getAccount(originId);
        BigDecimal newBalance = acc.getBalance().subtract(amount);
        acc.setBalance(newBalance);
        return acc;
    }

    public Account deposit(String id, BigDecimal amount) {
        try {
            Account acc = getAccount(id);
            BigDecimal newBalance = amount.add(acc.getBalance());
            acc.setBalance(newBalance);
            return acc;
        } catch (NoSuchElementException e) {
            return createAccount(id, amount);
        }
    }

    public Account getAccount(String id) {
        log.info("Finding accountId: " + id);
        return accounts.stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public Account createAccount(String accountId, BigDecimal amount) throws NoSuchElementException {
        log.info("Creating an account with id: " + accountId);
        Account acc = Account.builder()
                .id(accountId)
                .balance(amount)
                .build();

        this.accounts.add(acc);
        return getAccount(acc.getId());
    }

    public BigDecimal getBalance(String accountId) throws NoSuchElementException {
        return getAccount(accountId).getBalance();
    }

    public void reset() {
        log.info("Clearing accounts...");
        this.accounts.clear();
    }

    public Map<String, Account> transfer(AccountRequest request) throws NoSuchElementException{
        Map<String, Account> map = new HashMap<>();
        Account origin = withdraw(request.getOrigin(), request.getAmount());
        Account destination = deposit(request.getDestination(), request.getAmount());

        map.put("origin", origin);
        map.put("destination", destination);

        return map;
    }
}
