package com.pomkine.domain.account;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pomkine.domain.account.command.CreditAccount;
import com.pomkine.domain.account.command.DebitAccount;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.MoneyTransfer;
import com.pomkine.domain.transfer.MoneyTransferRepository;
import com.pomkine.domain.transfer.command.RecordNotFoundAccount;
import com.pomkine.domain.transfer.event.DebitRecorded;
import com.pomkine.domain.transfer.event.MoneyTransferCreated;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountsWorkflow {

    private AccountRepository accountRepository;
    private MoneyTransferRepository moneyTransferRepository;

    @Inject
    public AccountsWorkflow(EventBus eventBus,
                            AccountRepository accountRepository,
                            MoneyTransferRepository moneyTransferRepository) {
        eventBus.register(this);
        this.accountRepository = accountRepository;
        this.moneyTransferRepository = moneyTransferRepository;
    }

    @Subscribe
    public void credit(DebitRecorded debitRecorded) {
        AggregateId toAccountId = debitRecorded.getDetails().getToAccountId();
        Optional<Account> accountOptional = accountRepository.findById(toAccountId);
        accountOptional.ifPresent(account -> creditAccount(debitRecorded, account));
    }

    @Subscribe
    public void debit(MoneyTransferCreated transferCreated) {
        AggregateId transferId = transferCreated.getTransferId();
        AggregateId fromAccountId = transferCreated.getDetails().getFromAccountId();
        AggregateId toAccountId = transferCreated.getDetails().getToAccountId();

        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            Optional<Account> fromAccountOptional = accountRepository.findById(fromAccountId);
            Optional<Account> toAccountOptional = accountRepository.findById(toAccountId);

            if (fromAccountOptional.isPresent() && toAccountOptional.isPresent()) {
                Account toDebit = fromAccountOptional.get();
                debitAccount(transferCreated, toDebit);
            } else {
                if (!fromAccountOptional.isPresent()) {
                    recordNotFoundAccount(fromAccountId, transfer);
                }
                if (!toAccountOptional.isPresent()) {
                    recordNotFoundAccount(toAccountId, transfer);
                }
                moneyTransferRepository.save(transfer);
            }
        });
    }

    private void recordNotFoundAccount(AggregateId accountId, MoneyTransfer transfer) {
        transfer.recordNotFoundAccount(
            new RecordNotFoundAccount(transfer.getId(), accountId));
    }

    private void creditAccount(DebitRecorded debitRecorded, Account account) {
        account.credit(
            new CreditAccount(
                debitRecorded.getDetails().getAmount(),
                debitRecorded.getTransferId()));
        accountRepository.save(account);
    }

    private void debitAccount(MoneyTransferCreated transferCreated, Account toDebit) {
        toDebit.debit(
            new DebitAccount(
                transferCreated.getDetails().getAmount(),
                transferCreated.getTransferId()));
        accountRepository.save(toDebit);
    }

}
