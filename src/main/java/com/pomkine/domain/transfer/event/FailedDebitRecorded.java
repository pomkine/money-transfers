package com.pomkine.domain.transfer.event;

import com.pomkine.domain.transfer.TransferDetails;

public class FailedDebitRecorded extends MoneyTransferEvent {

    public FailedDebitRecorded(TransferDetails transferDetails) {
        super(transferDetails);
    }
}
