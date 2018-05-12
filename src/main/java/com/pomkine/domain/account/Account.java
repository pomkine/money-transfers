package com.pomkine.domain.account;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

import com.google.common.collect.Lists;
import com.pomkine.domain.account.command.CreditAccount;
import com.pomkine.domain.account.command.DebitAccount;
import com.pomkine.domain.account.command.OpenAccount;
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

    public Account open(OpenAccount open) {
        Money initialBalance = open.getInitialBalance();
        if (initialBalance.isNegative()) {
            throw new IllegalArgumentException(
                "Can't open an account with negative initial balance");
        }
        return handle(new AccountOpened(open.getAccountId(), initialBalance), true);
    }

    public Account credit(CreditAccount credit) {
        Money amount = credit.getAmount();
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Can't credit negative money amount");
        }
        return handle(new AccountCredited(id, amount, credit.getTransferId()), true);
    }

    public Account debit(DebitAccount debit) {
        Money amount = debit.getAmount();
        AggregateId transferId = debit.getTransferId();
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Can't debit negative money amount");
        }
        if (balance.isLessThan(amount)) {
            return handle(
                new AccountDebitFailedDueToInsufficientFunds(id, amount, transferId), true);
        }
        return handle(new AccountDebited(id, amount, transferId), true);
    }

    private Account debitFailed(AccountDebitFailedDueToInsufficientFunds debitFailed) {
        return this;
    }

    private Account debited(AccountDebited debited) {
        this.balance.minus(debited.getAmount());
        return this;
    }

    private Account credited(AccountCredited credited) {
        balance = balance.plus(credited.getAmount());
        return this;
    }

    private Account opened(AccountOpened opened) {
        balance = opened.getInitialBalance();
        id = opened.getAccountId();
        return this;
    }

    public List<AccountEvent> getPendingEvents() {
        return pendingEvents;
    }

    public void markEventsAsCommitted() {
        pendingEvents.clear();
    }

    private Account handle(AccountEvent event, boolean isNew) {
        if (isNew) {
            pendingEvents.add(event);
        }
        return Match(event).of(
            Case($(instanceOf(AccountOpened.class)), this::opened),
            Case($(instanceOf(AccountCredited.class)), this::credited),
            Case($(instanceOf(AccountDebited.class)), this::debited),
            Case($(instanceOf(AccountDebitFailedDueToInsufficientFunds.class)),
                this::debitFailed)
        );
    }
}
