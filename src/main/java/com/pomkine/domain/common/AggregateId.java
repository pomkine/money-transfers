package com.pomkine.domain.common;

import java.util.UUID;
import lombok.Data;

@Data
public class AggregateId {

    private final UUID id;

    private AggregateId() {
        this.id = UUID.randomUUID();
    }

    private AggregateId(String id) {
        this.id = UUID.fromString(id);
    }

    public static AggregateId generate() {
        return new AggregateId();
    }

    public static AggregateId from(String id) {
        return new AggregateId(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
