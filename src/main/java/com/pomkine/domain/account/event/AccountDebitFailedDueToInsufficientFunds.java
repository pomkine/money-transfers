package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountDebitFailedDueToInsufficientFunds extends AccountEvent {

    private final Money debitAmount;


    public AccountDebitFailedDueToInsufficientFunds(AggregateId accountId, Money debitAmount) {
        super(accountId);
        this.debitAmount = debitAmount;
    }
}
