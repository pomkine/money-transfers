package com.pomkine.domain.transfer;

import com.pomkine.domain.common.AggregateId;
import lombok.Builder;
import lombok.Value;
import org.joda.money.Money;

@Value
@Builder(builderClassName = "builder")
public class TransferDetails {

    private final AggregateId fromAccountId;
    private final AggregateId toAccountId;
    private final AggregateId transferId;
    private final Money transferAmount;
}
