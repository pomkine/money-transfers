package com.pomkine.domain.account

import com.pomkine.domain.account.event.AccountCredited
import com.pomkine.domain.account.event.AccountDebitFailedDueToInsufficientFunds
import com.pomkine.domain.account.event.AccountDebited
import com.pomkine.domain.account.event.AccountOpened
import com.pomkine.domain.common.AggregateId
import org.joda.money.Money
import spock.lang.Specification

import static com.pomkine.domain.account.AccountFixture.opened

class AccountSpec extends Specification {

    def accountId = AggregateId.generate()
    def transferId = AggregateId.generate()
    def TWO_HUNDRED_BUCKS = Money.parse("USD 200")
    def ONE_HUNDRED_BUCKS = Money.parse("USD 100")
    def NEGATIVE_MONEY_AMOUNT = Money.parse("USD -200")

    def "account is opened with initial balance and account id"() {
        setup:
        def account = new Account()

        when:
        account.open(accountId, TWO_HUNDRED_BUCKS)

        then:
        def events = account.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == AccountOpened
        and:
        event.accountId == accountId
        and:
        event.initialBalance == TWO_HUNDRED_BUCKS
    }

    def "can't open an account with negative initial balance"() {
        setup:
        def account = new Account()

        when:
        account.open(accountId, NEGATIVE_MONEY_AMOUNT)

        then:
        thrown(IllegalArgumentException)
    }

    def "credit money amount is added to opened account balance"() {
        setup:
        def account = opened(accountId, TWO_HUNDRED_BUCKS)

        when:
        account.credit(TWO_HUNDRED_BUCKS, transferId)

        then:
        def events = account.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == AccountCredited
        and:
        event.accountId == accountId
        and:
        event.amount == TWO_HUNDRED_BUCKS
        and:
        event.transferId == transferId
    }

    def "can't credit negative money amount"() {
        setup:
        def account = opened(accountId, TWO_HUNDRED_BUCKS)

        when:
        account.credit(NEGATIVE_MONEY_AMOUNT, transferId)

        then:
        thrown(IllegalArgumentException)
    }

    def "debit money amount is subtracted from account balance"() {
        setup:
        def account = opened(accountId, TWO_HUNDRED_BUCKS)

        when:
        account.debit(ONE_HUNDRED_BUCKS, transferId)

        then:
        def events = account.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == AccountDebited
        and:
        event.accountId == accountId
        and:
        event.amount == ONE_HUNDRED_BUCKS
        and:
        event.transferId == transferId
    }

    def "can't debit negative money amount"() {
        setup:
        def account = opened(accountId, TWO_HUNDRED_BUCKS)

        when:
        account.debit(NEGATIVE_MONEY_AMOUNT, transferId)

        then:
        thrown(IllegalArgumentException)
    }

    def "account debit failed if not enough money on balance"() {
        setup:
        def account = opened(accountId, ONE_HUNDRED_BUCKS)

        when:
        account.debit(TWO_HUNDRED_BUCKS, transferId)

        then:
        def events = account.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == AccountDebitFailedDueToInsufficientFunds
        and:
        event.accountId == accountId
        and:
        event.amount == TWO_HUNDRED_BUCKS
        and:
        event.transferId == transferId
    }


}
