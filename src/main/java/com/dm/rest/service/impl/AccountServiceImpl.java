package com.dm.rest.service.impl;

import com.dm.rest.exceptions.AccountException;
import com.dm.rest.persistance.entity.AccountEntity;
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
    public AccountEntity createAccount(AccountEntity accountEntity) {
        if (accountRepository.existsByLogin(accountEntity.getLogin())){
            throw new AccountException("login is already taken.");
        }
        return accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity getAccountByLogin(String login) {
        return accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountException("Account with login:" + log + " does not exist."));
    }

    @Override
    public List<AccountEntity> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public AccountEntity updateAccount(AccountEntity accountEntity) {
        AccountEntity acc = getAccountByLogin(accountEntity.getLogin());

        return accountRepository.save(acc);
    }

    @Override
    public void deleteAccount(String name) {
        AccountEntity accountEntity = getAccountByLogin(name);
        accountRepository.delete(accountEntity);
    }
}
