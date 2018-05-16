package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.money.Money;


@Getter
@EqualsAndHashCode(callSuper = true)
public class AccountOpened extends AccountEvent {

    private final Money initialBalance;

    public AccountOpened(AggregateId accountId, Money initialBalance) {
        super(accountId);
        this.initialBalance = initialBalance;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [account: %s, initial balance: %s]",
            this.getClass().getSimpleName(), getAccountId(), initialBalance);
    }
}
