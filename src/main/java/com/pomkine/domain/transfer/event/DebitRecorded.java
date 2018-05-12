package com.pomkine.domain.transfer.event;

import com.pomkine.domain.transfer.TransferDetails;

public class DebitRecorded extends MoneyTransferEvent {

    public DebitRecorded(TransferDetails transferDetails) {
        super(transferDetails);
    }
}
