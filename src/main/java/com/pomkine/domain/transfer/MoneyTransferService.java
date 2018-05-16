package com.pomkine.domain.transfer;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.command.CreateMoneyTransfer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.money.Money;

@Singleton
public class MoneyTransferService {

    private MoneyTransferRepository moneyTransferRepository;

    @Inject
    public MoneyTransferService(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public MoneyTransfer transferMoney(AggregateId fromAccountId, AggregateId toAccountId,
                                       Money amount) {
        return moneyTransferRepository.create(
            new CreateMoneyTransfer(TransferDetails.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .amount(amount)
                .build(), AggregateId.generate()));
    }

}
