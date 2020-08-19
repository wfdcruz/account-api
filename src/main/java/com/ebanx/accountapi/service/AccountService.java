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

    public void reset() {
        log.info("Clearing accounts...");
        this.accounts.clear();
    }

    public BigDecimal getBalance(String accountId) throws NoSuchElementException {
        log.info("Get Balance from accountId: " + accountId);
        return getAccount(accountId).getBalance();
    }

    public Map<String, Account> transfer(AccountRequest request) throws NoSuchElementException{
        log.info("Transfer: " + request.getAmount() + "from account: "
                + request.getOrigin() + "to account: " + request.getDestination());

        Map<String, Account> map = new HashMap<>();
        Account origin = withdraw(request.getOrigin(), request.getAmount());
        Account destination = deposit(request.getDestination(), request.getAmount());

        map.put("origin", origin);
        map.put("destination", destination);

        return map;
    }

    public Account withdraw(String originAccountId, BigDecimal amount) throws NoSuchElementException {
        log.info("Withdrawing: " + amount + "from account: " + originAccountId);
        Account acc = getAccount(originAccountId);
        BigDecimal newBalance = acc.getBalance().subtract(amount);
        acc.setBalance(newBalance);
        return acc;
    }

    public Account deposit(String destinationAccountId, BigDecimal amount) {
        log.info("Depositing: " + amount + "to account: " + destinationAccountId);
        try {
            Account acc = getAccount(destinationAccountId);
            BigDecimal newBalance = amount.add(acc.getBalance());
            acc.setBalance(newBalance);
            return acc;
        } catch (NoSuchElementException e) {
            return createAccount(destinationAccountId, amount);
        }
    }

    private Account createAccount(String accountId, BigDecimal amount) throws NoSuchElementException {
        log.info("Creating an account with id: " + accountId);

        Account acc = Account.builder()
                .id(accountId)
                .balance(amount)
                .build();

        this.accounts.add(acc);
        return getAccount(acc.getId());
    }

    private Account getAccount(String accountId) {
        log.info("Finding accountId: " + accountId);

        return accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

}
