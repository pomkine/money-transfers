package com.pomkine.application.guice;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.pomkine.domain.account.AccountRepository;
import com.pomkine.domain.account.AccountsWorkflow;
import com.pomkine.domain.transfer.MoneyTransferRepository;
import com.pomkine.domain.transfer.MoneyTransfersWorkflow;
import com.pomkine.eventstore.EventSourcesAccountRepository;
import com.pomkine.eventstore.EventSourcesMoneyTransferRepository;
import com.pomkine.eventstore.EventStore;
import com.pomkine.eventstore.EventStream;
import com.pomkine.eventstore.InMemoryEventStore;
import com.pomkine.eventstore.log.LoggingEventListener;
import java.util.List;

public class EventStoreModule extends AbstractModule {

    //    private EventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(10));
    private EventBus eventBus = new EventBus("domain-events");

    @Override
    // @formatter:off
    protected void configure() {
        bind(EventStore.class).to(InMemoryEventStore.class);
        bind(AccountRepository.class).to(EventSourcesAccountRepository.class);
        bind(MoneyTransferRepository.class).to(EventSourcesMoneyTransferRepository.class);
        bind(EventBus.class).toInstance(eventBus);
        bind(new TypeLiteral<List<EventStream>>(){})
            .annotatedWith(Names.named("eventStreams"))
            .toInstance(Lists.newCopyOnWriteArrayList());
        requireBinding(AccountsWorkflow.class);
        requireBinding(MoneyTransfersWorkflow.class);
        requireBinding(LoggingEventListener.class);
    }
    // @formatter:on
}
