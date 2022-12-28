package com.dm.rest.service;

import com.dm.rest.persistance.entity.AccountEntity;

import java.util.List;

public interface AccountService {

    AccountEntity createAccount(AccountEntity accountEntity);

    AccountEntity getAccountByLogin(String login);

    List<AccountEntity> getAllAccount();

    AccountEntity updateAccount(AccountEntity accountEntity);

    void deleteAccount(String name);
}
