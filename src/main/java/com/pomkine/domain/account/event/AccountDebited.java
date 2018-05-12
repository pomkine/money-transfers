package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountDebited extends AccountEvent {

    private final Money debitAmount;
    private final AggregateId transferId;

    public AccountDebited(AggregateId accountId, Money debitAmount, AggregateId transferId) {
        super(accountId);
        this.debitAmount = debitAmount;
        this.transferId = transferId;
    }
}
