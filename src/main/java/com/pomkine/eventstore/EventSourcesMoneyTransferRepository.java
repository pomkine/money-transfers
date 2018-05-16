package com.pomkine.eventstore;

import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.transfer.MoneyTransfer;
import com.pomkine.domain.transfer.MoneyTransferRepository;
import com.pomkine.domain.transfer.command.CreateMoneyTransfer;
import com.pomkine.domain.transfer.event.MoneyTransferEvent;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventSourcesMoneyTransferRepository implements MoneyTransferRepository {

    private EventStore eventStore;

    @Inject
    public EventSourcesMoneyTransferRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public MoneyTransfer save(MoneyTransfer transfer) {
        eventStore.save(MoneyTransfer.class, transfer.getId(), transfer.getPendingEvents());
        transfer.markEventsAsCommitted();
        return transfer;
    }

    @Override
    public Optional<MoneyTransfer> findById(AggregateId id) {
        List<MoneyTransferEvent> history = eventStore.loadEvents(MoneyTransfer.class, id);
        if (history.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(MoneyTransfer.from(history));
    }

    @Override
    public MoneyTransfer create(CreateMoneyTransfer createMoneyTransfer) {
        MoneyTransfer transfer = new MoneyTransfer();
        transfer = transfer.create(createMoneyTransfer);
        eventStore.save(MoneyTransfer.class, transfer.getId(), transfer.getPendingEvents());
        transfer.markEventsAsCommitted();
        return transfer;
    }
}
