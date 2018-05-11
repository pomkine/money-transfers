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

    private final Money creditAmount;

    public AccountCredited(AggregateId accountId, Money creditAmount) {
        super(accountId);
        this.creditAmount = creditAmount;
    }
}
