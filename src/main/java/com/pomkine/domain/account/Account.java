package com.pomkine.domain.account;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

import com.google.common.collect.Lists;
import com.pomkine.domain.account.event.AccountCredited;
import com.pomkine.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.pomkine.domain.account.event.AccountDebited;
import com.pomkine.domain.account.event.AccountEvent;
import com.pomkine.domain.account.event.AccountOpened;
import com.pomkine.domain.common.AggregateId;
import java.util.List;
import lombok.ToString;
import org.joda.money.Money;

@ToString
public class Account {

    private AggregateId id;
    private Money balance;
    private List<AccountEvent> pendingEvents = Lists.newArrayList();

    public static Account from(List<AccountEvent> history) {
        return ofAll(history)
            .foldLeft(new Account(), (account, event) -> account.handle(event, false));
    }

    public Account open(AggregateId id, Money initialBalance) {
        if (initialBalance.isNegative()) {
            throw new IllegalArgumentException(
                "Can't open an account with negative initial balance");
        }
        return handle(new AccountOpened(id, initialBalance), true);
    }

    public Account credit(Money creditAmount, AggregateId transferId) {
        if (creditAmount.isNegative()) {
            throw new IllegalArgumentException("Can't credit negative money amount");
        }
        return handle(new AccountCredited(id, creditAmount, transferId), true);
    }

    public Account debit(Money debitAmount, AggregateId transferId) {
        if (debitAmount.isNegative()) {
            throw new IllegalArgumentException("Can't debit negative money amount");
        }
        if (balance.isLessThan(debitAmount)) {
            return handle(
                new AccountDebitFailedDueToInsufficientFunds(id, debitAmount, transferId), true);
        }
        return handle(new AccountDebited(id, debitAmount, transferId), true);
    }

    private Account accountDebitFailed(AccountDebitFailedDueToInsufficientFunds debitFailed) {
        return this;
    }

    private Account accountDebited(AccountDebited accountDebited) {
        this.balance.minus(accountDebited.getDebitAmount());
        return this;
    }

    private Account accountCredited(AccountCredited accountCredited) {
        balance = balance.plus(accountCredited.getCreditAmount());
        return this;
    }

    private Account accountOpened(AccountOpened accountOpened) {
        balance = accountOpened.getInitialBalance();
        id = accountOpened.getAccountId();
        return this;
    }

    public List<AccountEvent> getPendingEvents() {
        return pendingEvents;
    }

    public void markEventsAsCommitted() {
        pendingEvents.clear();
    }

    private Account handle(AccountEvent accountEvent, boolean isNew) {
        if (isNew) {
            pendingEvents.add(accountEvent);
        }
        return Match(accountEvent).of(
            Case($(instanceOf(AccountOpened.class)), this::accountOpened),
            Case($(instanceOf(AccountCredited.class)), this::accountCredited),
            Case($(instanceOf(AccountDebited.class)), this::accountDebited),
            Case($(instanceOf(AccountDebitFailedDueToInsufficientFunds.class)),
                this::accountDebitFailed)
        );
    }
}
