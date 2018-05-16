package com.pomkine.domain.transfer.command;

import com.pomkine.domain.common.AggregateId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RecordNotFoundAccount {

    private final AggregateId transferId;
    private final AggregateId notFoundAccountId;

    public RecordNotFoundAccount(AggregateId transferId,
                                 AggregateId notFoundAccountId) {
        this.transferId = transferId;
        this.notFoundAccountId = notFoundAccountId;
    }
}
