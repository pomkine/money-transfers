package com.pomkine.boundary;


import com.pomkine.domain.account.Account;
import com.pomkine.domain.account.AccountService;
import javax.inject.Inject;
import org.joda.money.Money;

public class Accounts {

    private AccountService accountService;

    @Inject
    public Accounts(AccountService accountService) {
        this.accountService = accountService;
    }

    public String openAccount(Money initialBalance) {
        Account opened = accountService.openAccount(initialBalance);
        return opened.getId().toString();
    }
}
