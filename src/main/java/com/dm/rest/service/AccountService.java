package com.dm.rest.service;

import com.dm.rest.persistance.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountByLogin(String login);

    List<Account> getAllAccount();

    Account updateAccount(Account account);

    void deleteAccount(String name);
}
