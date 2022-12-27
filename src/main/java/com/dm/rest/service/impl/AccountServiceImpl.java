package com.dm.rest.service.impl;

import com.dm.rest.exceptions.AccountException;
import com.dm.rest.persistance.entity.Account;
import com.dm.rest.persistance.repository.AccountRepository;
import com.dm.rest.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        if (accountRepository.existsByLogin(account.getLogin())){
            throw new AccountException("login is already taken.");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByLogin(String login) {
        return accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountException("Account with login:" + log + " does not exist."));
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(Account account) {
        Account acc = getAccountByLogin(account.getLogin());

        return accountRepository.save(acc);
    }

    @Override
    public void deleteAccount(String name) {
        Account account = getAccountByLogin(name);
        accountRepository.delete(account);
    }
}
