package com.pomkine.domain.account.command;


import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode
@ToString
public class DebitAccount {

    private final Money amount;
    private final AggregateId transferId;

    public DebitAccount(Money amount, AggregateId transferId) {
        this.amount = amount;
        this.transferId = transferId;
    }
}
