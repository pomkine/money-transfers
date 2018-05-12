package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountCredited extends AccountEvent {

    private final Money amount;
    private final AggregateId transferId;

    public AccountCredited(AggregateId accountId, Money amount, AggregateId transferId) {
        super(accountId);
        this.amount = amount;
        this.transferId = transferId;
    }
}
