package com.dm.rest.controllers;

import com.dm.rest.persistance.entity.Account;
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
    public Account getAccount(@PathVariable("login") String login){
        return accountService.getAccountByLogin(login);
    }

    @GetMapping()
    public List<Account> getAllAccounts(){
        return accountService.getAllAccount();
    }

    @PostMapping
    public Account addAccount(@RequestBody Account account){
        return accountService.createAccount(account);
    }

    @PutMapping
    public Account updateAccount(@RequestBody Account account){
        return accountService.updateAccount(account);
    }

    @DeleteMapping("/{login}")
    public void deleteAccount(@PathVariable("login") String login){
        accountService.deleteAccount(login);
    }
}
