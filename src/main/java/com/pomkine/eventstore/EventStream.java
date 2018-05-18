package com.pomkine.eventstore;

import static java.util.Comparator.comparing;

import com.google.common.collect.Lists;
import com.pomkine.domain.common.Aggregate;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import com.pomkine.domain.common.Version;
import com.pomkine.eventstore.error.VersionConflict;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Slf4j
public class EventStream<S extends Aggregate<E>, E extends DomainEvent> {

    private final AggregateId aggregateId;
    private final Class<S> aggregateType;
    private final List<E> events;
    private Version version = Version.NONE;

    public EventStream(AggregateId aggregateId, Class<S> aggregateType) {
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.events = Lists.newArrayList();
    }

    public void addEvents(List<E> toAdd, Version version) {
        if (!this.hasSameVersion(version)) {
            throw new VersionConflict(aggregateType, aggregateId);
        }
        toAdd.sort(comparing(DomainEvent::getWhen));
        events.addAll(toAdd);
        increaseVersion();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    private void increaseVersion() {
        version = version.increased();
    }

    private boolean hasSameVersion(Version version) {
        return this.version.equals(version);
    }

    @Override
    public String toString() {
        return String.format(
            "EventStream[Aggregate: %s, ID: %s, Events count: %s, Version: %s]",
            aggregateType.getSimpleName(), aggregateId, events.size(), version);
    }
}
