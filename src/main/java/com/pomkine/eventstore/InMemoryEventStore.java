package com.pomkine.eventstore;

import static java.util.Comparator.comparing;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@SuppressWarnings("unchecked")
@Singleton
public class InMemoryEventStore implements EventStore {

    private List<EventStream> eventStreams;
    private EventBus eventBus;
    private TransactionManager txManager;

    @Inject
    public InMemoryEventStore(@Named("eventStreams") List<EventStream> eventStreams,
                              EventBus eventBus, TransactionManager txManager) {
        this.eventStreams = eventStreams;
        this.eventBus = eventBus;
        this.txManager = txManager;
    }

    @Override
    public void save(Class aggregateClass, AggregateId id, List<? extends DomainEvent> events) {
        txManager.doInTx(id, () ->
            addEventsToStream(aggregateClass, id, events));
        publishEvents(events);
    }

    @Override
    public <T extends DomainEvent> List<T> loadEvents(Class aggregateClass, AggregateId id) {
        List<T> events = Lists.newArrayList();
        txManager.doInTx(id, () -> {
            findEventStream(aggregateClass, id)
                .ifPresent(eventStream ->
                    events.addAll(eventStream.getEvents()));
        });
        return events;
    }

    private void addEventsToStream(Class aggregateClass, AggregateId id,
                                   List<? extends DomainEvent> events) {
        Optional<EventStream> esOpt = findEventStream(aggregateClass, id);
        events.sort(dateComparator());
        if (esOpt.isPresent()) {
            EventStream eventStream = esOpt.get();
            eventStream.addEvents(events);
        } else {
            createNewEventStream(aggregateClass, id, events);
        }
    }

    private void publishEvents(List<? extends DomainEvent> events) {
        events.forEach(event -> eventBus.post(event));
    }

    private void createNewEventStream(Class aggregateClass, AggregateId id,
                                      List<? extends DomainEvent> events) {
        EventStream newEventStream = new EventStream(id, aggregateClass);
        newEventStream.addEvents(events);
        eventStreams.add(newEventStream);
    }

    private Comparator<DomainEvent> dateComparator() {
        return comparing(DomainEvent::getWhen);
    }

    private Optional<EventStream> findEventStream(Class aggregateClass, AggregateId id) {
        return eventStreams.stream()
            .filter(s -> s.getAggregateType().equals(aggregateClass))
            .filter(s -> s.getAggregateId().equals(id))
            .findFirst();
    }
}
