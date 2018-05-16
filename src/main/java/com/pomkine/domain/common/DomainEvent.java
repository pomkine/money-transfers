package com.pomkine.domain.common;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DomainEvent implements Comparable<DomainEvent> {

    private final Instant when;

    public DomainEvent() {
        this.when = Instant.now();
    }

    @Override
    public int compareTo(DomainEvent other) {
        return this.when.compareTo(other.when);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomainEvent)) {
            return false;
        }
        DomainEvent that = (DomainEvent) o;
        return Objects.equals(when, that.when);
    }

    @Override
    public int hashCode() {
        return Objects.hash(when);
    }
}
