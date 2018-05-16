package com.pomkine.domain.transfer.command;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.TransferDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CreateMoneyTransfer {

    private final TransferDetails details;
    private final AggregateId transferId;

    public CreateMoneyTransfer(TransferDetails details, AggregateId transferId) {
        this.details = details;
        this.transferId = transferId;
    }
}
