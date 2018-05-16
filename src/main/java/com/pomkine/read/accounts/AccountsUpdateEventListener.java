package com.pomkine.read.accounts;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pomkine.domain.account.event.AccountCredited;
import com.pomkine.domain.account.event.AccountDebited;
import com.pomkine.domain.account.event.AccountOpened;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;

@Slf4j
@Singleton
public class AccountsUpdateEventListener {

    private final Map<String, AccountInfoResponse> responses;

    @Inject
    public AccountsUpdateEventListener(EventBus eventBus,
                                       @Named("accountInfoResponses")
                                           Map<String, AccountInfoResponse> responses) {
        eventBus.register(this);
        this.responses = responses;
    }

    @Subscribe
    public void credited(AccountCredited credited) {
        String accountId = credited.getAccountId().toString();
        Optional<AccountInfoResponse> accountInfoOpt = find(accountId);
        accountInfoOpt.ifPresent(old -> {
            addMoney(old, credited.getAmount());
        });
    }

    @Subscribe
    public void debited(AccountDebited debited) {
        String accountId = debited.getAccountId().toString();
        Optional<AccountInfoResponse> accountInfoOpt = find(accountId);
        accountInfoOpt.ifPresent(old -> {
            subtractMoney(old, debited.getAmount());
        });
    }

    @Subscribe
    public void opened(AccountOpened opened) {
        String idAsString = opened.getAccountId().toString();
        responses.put(idAsString,
            new AccountInfoResponse(
                idAsString, opened.getInitialBalance()));
    }

    private void addMoney(AccountInfoResponse old, Money amount) {
        responses
            .put(old.getAccountId(),
                new AccountInfoResponse(old.getAccountId(), old.getBalance().plus(amount)));
    }

    private void subtractMoney(AccountInfoResponse old, Money amount) {
        responses
            .put(old.getAccountId(),
                new AccountInfoResponse(old.getAccountId(), old.getBalance().minus(amount)));
    }

    private Optional<AccountInfoResponse> find(String accountId) {
        return Optional.ofNullable(responses.get(accountId));
    }

}
