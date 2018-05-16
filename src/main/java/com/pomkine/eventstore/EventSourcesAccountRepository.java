package com.pomkine.eventstore;

import com.pomkine.domain.account.Account;
import com.pomkine.domain.account.AccountRepository;
import com.pomkine.domain.account.command.OpenAccount;
import com.pomkine.domain.account.event.AccountEvent;
import com.pomkine.domain.common.AggregateId;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventSourcesAccountRepository implements AccountRepository {

    private EventStore eventStore;

    @Inject
    public EventSourcesAccountRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Account save(Account account) {
        eventStore.save(Account.class, account.getId(), account.getPendingEvents());
        account.markEventsAsCommitted();
        return account;
    }

    @Override
    public Optional<Account> findById(AggregateId id) {
        List<AccountEvent> history = eventStore.loadEvents(Account.class, id);
        if (history.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Account.from(history));
    }

    @Override
    public Account openAccount(OpenAccount openAccount) {
        Account account = new Account();
        account = account.open(openAccount);
        eventStore.save(Account.class, account.getId(), account.getPendingEvents());
        account.markEventsAsCommitted();
        return account;
    }
}
