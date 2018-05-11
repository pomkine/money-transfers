package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class AccountEvent extends DomainEvent {

    private final AggregateId accountId;

    public AccountEvent(AggregateId accountId) {
        super();
        this.accountId = accountId;
    }
}
