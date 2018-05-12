package com.pomkine.domain.transfer;

import static com.google.common.collect.Lists.newArrayList;

import com.pomkine.domain.transfer.event.MoneyTransferCreated;

public class MoneyTransferFixture {

    public static MoneyTransfer created(TransferDetails details) {
        return MoneyTransfer.from(newArrayList(new MoneyTransferCreated(details)));
    }

}
