package com.pomkine.domain.common;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class DomainEvent {

    private final Instant when;

    public DomainEvent() {
        this.when = Instant.now();
    }
}
