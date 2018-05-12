package com.pomkine.domain.transfer;

import com.google.common.collect.Lists;
import com.pomkine.domain.transfer.event.MoneyTransferCreated;

public class MoneyTransferFixture {

    public static MoneyTransfer created(TransferDetails transferDetails) {
        return MoneyTransfer.from(Lists.newArrayList(
            new MoneyTransferCreated(transferDetails)));
    }

}
