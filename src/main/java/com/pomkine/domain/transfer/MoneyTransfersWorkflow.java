package com.pomkine.domain.transfer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pomkine.domain.account.event.AccountCredited;
import com.pomkine.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.pomkine.domain.account.event.AccountDebited;
import com.pomkine.domain.common.AggregateId;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoneyTransfersWorkflow {

    private MoneyTransferRepository moneyTransferRepository;

    @Inject
    public MoneyTransfersWorkflow(EventBus eventBus,
                                  MoneyTransferRepository moneyTransferRepository) {
        eventBus.register(this);
        this.moneyTransferRepository = moneyTransferRepository;
    }

    @Subscribe
    public void recordDebit(AccountDebited accountDebited) {
        AggregateId transferId = accountDebited.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordDebit();
            moneyTransferRepository.save(transfer);
        });
    }

    @Subscribe
    public void recordCredit(AccountCredited accountCredited) {
        AggregateId transferId = accountCredited.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordCredit();
            moneyTransferRepository.save(transfer);
        });
    }

    @Subscribe
    public void recordFailedDebit(AccountDebitFailedDueToInsufficientFunds debitFailed) {
        AggregateId transferId = debitFailed.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordFailedDebit();
            moneyTransferRepository.save(transfer);
        });
    }


}
