package com.pomkine.eventstore.log;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pomkine.domain.common.DomainEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class LoggingEventListener {

    @Inject
    public LoggingEventListener(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void logDomainEvent(DomainEvent domainEvent) {
        log.trace("{}", domainEvent);
    }

    @Subscribe
    public void logDeadEvent(DeadEvent deadEvent) {
        log.warn("Dead event: {}", deadEvent);
    }

}
