package com.pomkine.domain.transfer.event;


import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.TransferDetails;
import lombok.Getter;

@Getter
public class MoneyTransferCreated extends MoneyTransferEvent {

    public MoneyTransferCreated(AggregateId transferId, TransferDetails details) {
        super(transferId, details);
    }

    @Override
    public String toString() {
        return String.format(
            "%s [transfer: %s, from: %s, to: %s, amount: %s]",
            this.getClass().getSimpleName(), getTransferId(), getDetails().getFromAccountId(),
            getDetails().getToAccountId(), getDetails().getAmount());
    }
}
