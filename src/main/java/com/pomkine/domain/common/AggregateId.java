package com.pomkine.domain.common;

import java.util.UUID;
import lombok.Data;

@Data
public class AggregateId {

    private final UUID id;

    private AggregateId() {
        this.id = UUID.randomUUID();
    }

    public static AggregateId generate() {
        return new AggregateId();
    }
}
