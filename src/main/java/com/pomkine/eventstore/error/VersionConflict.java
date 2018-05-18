package com.pomkine.eventstore.error;

import com.pomkine.domain.common.AggregateId;

public class VersionConflict extends RuntimeException {

    public VersionConflict(Class aggregateClass, AggregateId id) {
        super(String.format(
            "Version conflict. Aggregate class: %s, Aggregate id: %s",
            aggregateClass.getSimpleName(), id
        ));
    }
}
