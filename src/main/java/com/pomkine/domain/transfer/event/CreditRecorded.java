package com.pomkine.domain.transfer.event;

import com.pomkine.domain.transfer.TransferDetails;

public class CreditRecorded extends MoneyTransferEvent {

    public CreditRecorded(TransferDetails transferDetails) {
        super(transferDetails);
    }
}
