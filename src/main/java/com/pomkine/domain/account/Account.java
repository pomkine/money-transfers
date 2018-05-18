package com.pomkine.domain.account;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

import com.pomkine.domain.account.command.CreditAccount;
import com.pomkine.domain.account.command.DebitAccount;
import com.pomkine.domain.account.command.OpenAccount;
import com.pomkine.domain.account.event.AccountCredited;
import com.pomkine.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.pomkine.domain.account.event.AccountDebited;
import com.pomkine.domain.account.event.AccountEvent;
import com.pomkine.domain.account.event.AccountOpened;
import com.pomkine.domain.common.Aggregate;
import com.pomkine.domain.common.AggregateId;
import com.pomkine.domain.common.Version;
import java.util.List;
import lombok.ToString;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

@ToString
public class Account extends Aggregate<AccountEvent> {

    private Money balance;

    public static Account from(List<AccountEvent> history, Version version) {
        return (Account) ofAll(history)
            .foldLeft(new Account(), (account, event) -> account.handle(event, false))
            .setVersion(version);
    }

    public Account open(OpenAccount open) {
        Money initialBalance = open.getInitialBalance();
        checkCurrencySupported(initialBalance);
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

    private void checkCurrencySupported(Money initialBalance) {
        CurrencyUnit currencyUnit = initialBalance.getCurrencyUnit();
        if (!currencyUnit.equals(CurrencyUnit.USD)) {
            String errorMsg = String.format(
                "Only USD currency accounts can be opened. Requested currency: %s", currencyUnit);
            throw new IllegalArgumentException(errorMsg);
        }
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
