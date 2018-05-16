package com.pomkine.boundary;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.MoneyTransferService;
import javax.inject.Inject;
import org.joda.money.Money;

public class MoneyTransfers {

    private MoneyTransferService moneyTransferService;

    @Inject
    public MoneyTransfers(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    public String transferMoney(String fromAccountId, String toAccountId, Money amount) {
        return moneyTransferService
            .transferMoney(
                AggregateId.from(fromAccountId),
                AggregateId.from(toAccountId),
                amount)
            .getId().toString();
    }

}
