package com.pomkine.domain.transfer.event;

import com.pomkine.domain.common.DomainEvent;
import com.pomkine.domain.transfer.TransferDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class MoneyTransferEvent extends DomainEvent {

    private final TransferDetails transferDetails;

    public MoneyTransferEvent(TransferDetails transferDetails) {
        super();
        this.transferDetails = transferDetails;
    }
}
