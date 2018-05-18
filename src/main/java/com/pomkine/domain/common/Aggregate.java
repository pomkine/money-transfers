package com.pomkine.domain.common;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Getter;

public abstract class Aggregate<EventType extends DomainEvent> {

    @Getter
    protected AggregateId id;
    @Getter
    protected Version version;
    protected List<EventType> pendingEvents = Lists.newArrayList();

    protected Aggregate<EventType> setVersion(Version version) {
        this.version = version;
        return this;
    }

    public void markEventsAsCommitted() {
        pendingEvents.clear();
    }

    public List<EventType> getPendingEvents() {
        return pendingEvents;
    }
}
