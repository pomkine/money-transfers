package com.pomkine.domain.transfer.event;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import com.pomkine.domain.transfer.TransferDetails;
import java.util.Objects;
import lombok.Getter;

@Getter
public abstract class MoneyTransferEvent extends DomainEvent {

    private final AggregateId transferId;
    private final TransferDetails details;

    public MoneyTransferEvent(AggregateId transferId, TransferDetails details) {
        super();
        this.transferId = transferId;
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MoneyTransferEvent that = (MoneyTransferEvent) o;
        return Objects.equals(transferId, that.transferId) &&
            Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transferId, details);
    }
}
