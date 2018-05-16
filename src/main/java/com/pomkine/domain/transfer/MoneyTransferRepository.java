package com.pomkine.domain.transfer;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.command.CreateMoneyTransfer;
import java.util.Optional;

public interface MoneyTransferRepository {

    MoneyTransfer save(MoneyTransfer transfer);

    Optional<MoneyTransfer> findById(AggregateId id);

    MoneyTransfer create(CreateMoneyTransfer createMoneyTransfer);

}
