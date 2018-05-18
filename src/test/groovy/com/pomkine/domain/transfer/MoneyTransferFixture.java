package com.pomkine.domain.transfer;

import static com.google.common.collect.Lists.newArrayList;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.Version;
import com.pomkine.domain.transfer.event.MoneyTransferCreated;

public class MoneyTransferFixture {

    public static MoneyTransfer created(AggregateId transferId, TransferDetails details) {
        return MoneyTransfer.from(
            newArrayList(new MoneyTransferCreated(transferId, details)), Version.of(1));
    }

}
