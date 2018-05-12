package com.pomkine.domain.account.command;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode
@ToString
public class OpenAccount {

    private final AggregateId accountId;
    private final Money initialBalance;

    public OpenAccount(AggregateId accountId, Money initialBalance) {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
    }
}
