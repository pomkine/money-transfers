package com.pomkine.domain.transfer.event;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.TransferDetails;

public class CreditRecorded extends MoneyTransferEvent {

    public CreditRecorded(AggregateId transferId, TransferDetails transferDetails) {
        super(transferId, transferDetails);
    }

    @Override
    public String toString() {
        return String.format(
            "%s [transfer: %s, from: %s, to: %s, amount: %s]",
            this.getClass().getSimpleName(), getTransferId(), getDetails().getFromAccountId(),
            getDetails().getToAccountId(), getDetails().getAmount());
    }
}
