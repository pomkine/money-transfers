package com.pomkine.domain.account;

import com.pomkine.domain.account.command.OpenAccount;
import com.pomkine.domain.common.AggregateId;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.money.Money;

@Singleton
public class AccountService {

    private AccountRepository accountRepository;

    @Inject
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account openAccount(Money initialBalance) {
        AggregateId accountId = AggregateId.generate();
        OpenAccount openAccountCommand = new OpenAccount(accountId, initialBalance);
        return accountRepository.openAccount(openAccountCommand);
    }

}
