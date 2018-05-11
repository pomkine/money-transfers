package com.pomkine.domain.account;

import static com.google.common.collect.Lists.newArrayList;

import com.pomkine.domain.account.event.AccountOpened;
import com.pomkine.domain.common.AggregateId;
import org.joda.money.Money;

public class AccountFixture {

    public static Account opened(AggregateId accountId, Money initialBalance) {
        return Account.from(newArrayList(new AccountOpened(accountId, initialBalance)));
    }

}
