package com.pomkine.eventstore;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import java.util.List;

public interface EventStore {

    void save(Class aggregateClass, AggregateId id, List<? extends DomainEvent> events);

    <T extends DomainEvent> List<T> loadEvents(Class aggregateClass, AggregateId id);
}
