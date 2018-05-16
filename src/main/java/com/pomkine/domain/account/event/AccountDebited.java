package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AccountDebited extends AccountEvent {

    private final Money amount;
    private final AggregateId transferId;

    public AccountDebited(AggregateId accountId, Money amount, AggregateId transferId) {
        super(accountId);
        this.amount = amount;
        this.transferId = transferId;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [account: %s,amount: %s, transfer: %s]",
            this.getClass().getSimpleName(), getAccountId(), amount, transferId);
    }
}
