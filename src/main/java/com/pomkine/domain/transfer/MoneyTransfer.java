package com.pomkine.domain.transfer;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

import com.google.common.collect.Lists;
import com.pomkine.domain.transfer.event.CreditRecorded;
import com.pomkine.domain.transfer.event.DebitRecorded;
import com.pomkine.domain.transfer.event.FailedDebitRecorded;
import com.pomkine.domain.transfer.event.MoneyTransferCreated;
import com.pomkine.domain.transfer.event.MoneyTransferEvent;
import java.util.List;
import lombok.ToString;


@ToString
public class MoneyTransfer {

    private TransferDetails transferDetails;
    private MoneyTransferState state;

    private List<MoneyTransferEvent> pendingEvents = Lists.newArrayList();

    public static MoneyTransfer from(List<MoneyTransferEvent> history) {
        return ofAll(history)
            .foldLeft(new MoneyTransfer(), (transfer, event)
                -> transfer.handle(event, false));
    }

    public MoneyTransfer create(TransferDetails transferDetails) {
        if (transferDetails.getTransferAmount().isNegativeOrZero()) {
            throw new IllegalArgumentException(
                "Can't create money amount with negative or zero transfer amount");
        }
        if (sameAccount(transferDetails)) {
            throw new IllegalArgumentException(
                "Can't create money transfer using the same account for debit and credit");
        }
        return handle(new MoneyTransferCreated(transferDetails), true);
    }

    public MoneyTransfer recordDebit() {
        return handle(new DebitRecorded(transferDetails), true);
    }

    public MoneyTransfer recordCredit() {
        return handle(new CreditRecorded(transferDetails), true);
    }

    public MoneyTransfer recordFailedCredit() {
        return handle(new FailedDebitRecorded(transferDetails), true);
    }

    private boolean sameAccount(TransferDetails transferDetails) {
        return transferDetails.getFromAccountId().equals(transferDetails.getToAccountId());
    }

    private MoneyTransfer transferCreated(MoneyTransferCreated transferCreated) {
        transferDetails = transferCreated.getTransferDetails();
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

    public List<MoneyTransferEvent> getPendingEvents() {
        return pendingEvents;
    }

    public MoneyTransfer handle(MoneyTransferEvent transferEvent, boolean isNew) {
        if (isNew) {
            this.pendingEvents.add(transferEvent);
        }
        return Match(transferEvent).of(
            Case($(instanceOf(MoneyTransferCreated.class)), this::transferCreated),
            Case($(instanceOf(DebitRecorded.class)), this::debitRecorded),
            Case($(instanceOf(FailedDebitRecorded.class)), this::failedDebitRecorded),
            Case($(instanceOf(CreditRecorded.class)), this::creditRecorded)
        );

    }
}
