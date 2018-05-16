package com.pomkine.domain.account;

import com.google.common.collect.Lists;
import com.pomkine.domain.account.event.AccountCredited;
import com.pomkine.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.pomkine.domain.account.event.AccountDebited;
import com.pomkine.domain.account.event.AccountEvent;
import com.pomkine.domain.account.event.AccountOpened;
import com.pomkine.domain.common.AggregateId;
import java.util.List;
import org.joda.money.Money;

public class AccountFixture {

    public static Account opened(AggregateId id, Money initialBalance) {
        List<AccountEvent> events = Lists.newArrayList();
        events.add(new AccountOpened(id, initialBalance));
        return Account.from(events);
    }

    public static AccountOpened openedEvent(AggregateId id, Money initialBalance) {
        return new AccountOpened(id, initialBalance);
    }

    public static AccountCredited creditedEvent(AggregateId id, Money credit,
                                                AggregateId transferId) {
        return new AccountCredited(id, credit, transferId);
    }

    public static AccountDebited debitedEvent(AggregateId id, Money debit,
                                              AggregateId transferId) {
        return new AccountDebited(id, debit, transferId);
    }

    public static AccountDebitFailedDueToInsufficientFunds debitFailedEvent(AggregateId id,
                                                                            Money debit,
                                                                            AggregateId transferId) {
        return new AccountDebitFailedDueToInsufficientFunds(id, debit, transferId);
    }

}
