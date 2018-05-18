package com.pomkine.eventstore;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import com.pomkine.domain.common.Version;
import java.util.List;

public interface EventStore {

    void save(Class aggregateClass, AggregateId id,
              List<? extends DomainEvent> events, Version version);

    EventStream loadEventStream(Class aggregateClass, AggregateId id);
}
