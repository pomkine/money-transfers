package com.pomkine.eventstore;

import com.google.common.collect.Lists;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Slf4j
public class EventStream<S> {

    private final AggregateId aggregateId;
    private final Class<S> aggregateType;
    private final List<DomainEvent> events;

    public EventStream(AggregateId aggregateId, Class<S> aggregateType) {
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.events = Lists.newArrayList();
    }

    public void addEvents(List<DomainEvent> toAdd) {
        events.addAll(toAdd);
    }
}
