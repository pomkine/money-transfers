package com.pomkine.eventstore

import com.google.common.collect.Lists
import com.google.common.eventbus.EventBus
import com.pomkine.domain.account.Account
import com.pomkine.domain.account.event.AccountCredited
import com.pomkine.domain.account.event.AccountDebited
import com.pomkine.domain.account.event.AccountEvent
import com.pomkine.domain.account.event.AccountOpened
import com.pomkine.domain.common.AggregateId
import org.joda.money.Money
import spock.lang.Specification

class InMemoryEventStoreTest extends Specification {
    def accountId = AggregateId.generate()
    def eventStore = new InMemoryEventStore(
            Lists.newArrayList(), new EventBus(), new TransactionManager())


    def "event store should store and load events"() {
        setup:
        List<AccountEvent> events = generateEvents()

        when:
        eventStore.save(Account, accountId, events)

        then:
        def loadedEvents = eventStore.loadEvents(Account, accountId)
        loadedEvents == events
    }

    def "event store should return no events if no events to load"() {
        when:
        def loadedEvents = eventStore.loadEvents(Account, accountId)

        then:
        loadedEvents.isEmpty()
    }

    def "event store should append new events to old ones"() {
        setup:
        List<AccountEvent> oldEvents = generateEvents()
        eventStore.save(Account, accountId, oldEvents)

        when:
        List<AccountEvent> newEvents = generateEvents()
        eventStore.save(Account, accountId, newEvents)

        then:
        def loadedEvents = eventStore.loadEvents(Account, accountId)
        loadedEvents.size() == oldEvents.size() + newEvents.size()
        loadedEvents.containsAll(oldEvents)
        loadedEvents.containsAll(newEvents)
    }

    def "event store should load events ordered by time"() {
        setup:
        List<AccountEvent> events = generateEvents()
        eventStore.save(Account, accountId, events)

        when:
        def loaded = eventStore.loadEvents(Account, accountId)

        then:
        loaded == events.sort(false, new OrderBy<AccountEvent>({ it.getWhen() }))
    }

    private List<AccountEvent> generateEvents() {
        List<AccountEvent> events = Lists.newArrayList()
        def transferId = AggregateId.generate()
        def first = new AccountOpened(accountId, Money.parse("USD 200"))
        def second = new AccountCredited(accountId, Money.parse("USD 100"), transferId)
        def third = new AccountDebited(accountId, Money.parse("USD 300"), transferId)
        events.add(first)
        events.add(second)
        events.add(third)
        return events
    }
}
