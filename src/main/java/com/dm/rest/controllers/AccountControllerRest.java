package com.dm.rest.controllers;

import com.dm.rest.persistance.entity.AccountEntity;
import com.dm.rest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountControllerRest {

    private final AccountService accountService;

    @Autowired
    public AccountControllerRest(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{login}")
    public AccountEntity getAccount(@PathVariable("login") String login){
        return accountService.getAccountByLogin(login);
    }

    @GetMapping()
    public List<AccountEntity> getAllAccounts(){
        return accountService.getAllAccount();
    }

    @PostMapping
    public AccountEntity addAccount(@RequestBody AccountEntity accountEntity){
        return accountService.createAccount(accountEntity);
    }

    @PutMapping
    public AccountEntity updateAccount(@RequestBody AccountEntity accountEntity){
        return accountService.updateAccount(accountEntity);
    }

    @DeleteMapping("/{login}")
    public void deleteAccount(@PathVariable("login") String login){
        accountService.deleteAccount(login);
    }
}
