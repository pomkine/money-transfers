package com.pomkine.domain.transfer.event;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.TransferDetails;
import lombok.Getter;

@Getter
public class AccountNotFound extends MoneyTransferEvent {

    private final AggregateId notFoundAccountId;

    public AccountNotFound(AggregateId transferId, TransferDetails details,
                           AggregateId notFoundAccountId) {
        super(transferId, details);
        this.notFoundAccountId = notFoundAccountId;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [transfer: %s, from: %s, to: %s, amount: %s]",
            this.getClass().getSimpleName(), getTransferId(), getDetails().getFromAccountId(),
            getDetails().getToAccountId(), getDetails().getAmount());
    }
}
