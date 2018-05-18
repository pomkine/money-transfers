package com.pomkine.eventstore;

import com.google.common.eventbus.EventBus;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.DomainEvent;
import com.pomkine.domain.common.Version;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
    public void save(Class aggregateClass, AggregateId id,
                     List<? extends DomainEvent> events, Version version) {
        txManager.doInTx(id, () -> {
            EventStream eventStream =
                findEventStream(aggregateClass, id)
                    .orElseGet(() -> createNewEventStream(aggregateClass, id));
            eventStream.addEvents(events, version);
        });
        publishEvents(events);
    }

    @Override
    public EventStream loadEventStream(Class aggregateClass, AggregateId id) {
        AtomicReference<EventStream> es = new AtomicReference<>();
        txManager.doInTx(id, () -> {
            es.set(findEventStream(aggregateClass, id)
                .orElseGet(() -> new EventStream(id, aggregateClass)));
        });
        return es.get();
    }

    private void publishEvents(List<? extends DomainEvent> events) {
        events.forEach(event -> eventBus.post(event));
    }

    private EventStream createNewEventStream(Class aggregateClass, AggregateId id) {
        EventStream newEventStream = new EventStream(id, aggregateClass);
        eventStreams.add(newEventStream);
        return newEventStream;
    }

    private Optional<EventStream> findEventStream(Class aggregateClass, AggregateId id) {
        return eventStreams.stream()
            .filter(s -> s.getAggregateType().equals(aggregateClass))
            .filter(s -> s.getAggregateId().equals(id))
            .findFirst();
    }
}
