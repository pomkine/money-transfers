package com.pomkine.domain.transfer;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

import com.pomkine.domain.common.Aggregate;
import com.pomkine.domain.common.Version;
import com.pomkine.domain.transfer.command.CreateMoneyTransfer;
import com.pomkine.domain.transfer.command.RecordNotFoundAccount;
import com.pomkine.domain.transfer.event.AccountNotFound;
import com.pomkine.domain.transfer.event.CreditRecorded;
import com.pomkine.domain.transfer.event.DebitRecorded;
import com.pomkine.domain.transfer.event.FailedDebitRecorded;
import com.pomkine.domain.transfer.event.MoneyTransferCreated;
import com.pomkine.domain.transfer.event.MoneyTransferEvent;
import java.util.List;
import lombok.ToString;


@ToString
public class MoneyTransfer extends Aggregate<MoneyTransferEvent> {

    private TransferDetails details;
    private MoneyTransferState state;

    public static MoneyTransfer from(List<MoneyTransferEvent> history, Version version) {
        return (MoneyTransfer) ofAll(history)
            .foldLeft(new MoneyTransfer(), (transfer, event)
                -> transfer.handle(event, false))
            .setVersion(version);
    }

    public MoneyTransfer create(CreateMoneyTransfer create) {
        TransferDetails details = create.getDetails();
        if (details.getAmount().isNegativeOrZero()) {
            throw new IllegalArgumentException(
                "Can't create money transfer with negative or zero transfer amount");
        }
        if (sameAccount(details)) {
            throw new IllegalArgumentException(
                "Can't create money transfer using the same account for debit and credit");
        }
        return handle(new MoneyTransferCreated(create.getTransferId(), details), true);
    }

    public MoneyTransfer recordNotFoundAccount(RecordNotFoundAccount notFoundAccount) {
        return handle(
            new AccountNotFound(id, details, notFoundAccount.getNotFoundAccountId()), true);
    }

    public MoneyTransfer recordDebit() {
        return handle(new DebitRecorded(id, details), true);
    }

    public MoneyTransfer recordCredit() {
        return handle(new CreditRecorded(id, details), true);
    }

    public MoneyTransfer recordFailedDebit() {
        return handle(new FailedDebitRecorded(id, details), true);
    }

    private boolean sameAccount(TransferDetails transferDetails) {
        return transferDetails.getFromAccountId().equals(transferDetails.getToAccountId());
    }

    private MoneyTransfer created(MoneyTransferCreated transferCreated) {
        id = transferCreated.getTransferId();
        details = transferCreated.getDetails();
        state = MoneyTransferState.INITIAL;
        return this;
    }

    private MoneyTransfer debitRecorded(DebitRecorded debitRecorded) {
        state = MoneyTransferState.DEBITED;
        return this;
    }

    private MoneyTransfer creditRecorded(CreditRecorded creditRecorded) {
        state = MoneyTransferState.COMPLETED;
        return this;
    }

    private MoneyTransfer failedDebitRecorded(FailedDebitRecorded failedDebitRecorded) {
        state = MoneyTransferState.FAILED;
        return this;
    }

    private MoneyTransfer accountNotFound(AccountNotFound accountNotFound) {
        state = MoneyTransferState.FAILED;
        return this;
    }

    private MoneyTransfer handle(MoneyTransferEvent event, boolean isNew) {
        if (isNew) {
            this.pendingEvents.add(event);
        }
        return Match(event).of(
            Case($(instanceOf(MoneyTransferCreated.class)), this::created),
            Case($(instanceOf(DebitRecorded.class)), this::debitRecorded),
            Case($(instanceOf(FailedDebitRecorded.class)), this::failedDebitRecorded),
            Case($(instanceOf(AccountNotFound.class)), this::accountNotFound),
            Case($(instanceOf(CreditRecorded.class)), this::creditRecorded)
        );

    }
}
