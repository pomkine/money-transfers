package com.pomkine.domain.account.event;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AccountEvent extends DomainEvent {

    private final AggregateId accountId;

    public AccountEvent(AggregateId accountId) {
        super();
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AccountEvent that = (AccountEvent) o;
        return Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountId);
    }
}
