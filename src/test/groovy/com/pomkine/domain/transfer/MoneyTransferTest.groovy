package com.pomkine.domain.transfer

import com.pomkine.domain.common.AggregateId
import com.pomkine.domain.transfer.command.CreateMoneyTransfer
import com.pomkine.domain.transfer.command.RecordNotFoundAccount
import com.pomkine.domain.transfer.event.*
import org.joda.money.Money
import spock.lang.Specification

import static com.pomkine.domain.transfer.MoneyTransferFixture.created

class MoneyTransferTest extends Specification {

    def fromAccountId = AggregateId.generate()
    def toAccountId = AggregateId.generate()
    def transferId = AggregateId.generate()
    def ONE_HUNDRED_BUCKS = Money.parse("USD 100")
    def NEGATIVE_MONEY_AMOUNT = Money.parse("USD -200")
    def ZERO_MONEY_AMOUNT = Money.parse("USD 0")

    def transfer = new MoneyTransfer()

    def "money transfer is created with transfer details"() {
        setup:
        def transferDetails = detailsWithTransferAmount(ONE_HUNDRED_BUCKS)

        when:
        transfer.create(new CreateMoneyTransfer(transferDetails, transferId))

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == MoneyTransferCreated
        and:
        event.details == transferDetails
    }

    def "can't create money transfer with negative transfer amount"() {
        setup:
        def transferDetails = detailsWithTransferAmount(NEGATIVE_MONEY_AMOUNT)

        when:
        transfer.create(new CreateMoneyTransfer(transferDetails, transferId))

        then:
        thrown(IllegalArgumentException)
    }

    def "can't create money transfer with zero transfer amount"() {
        setup:
        def transferDetails = detailsWithTransferAmount(ZERO_MONEY_AMOUNT)

        when:
        transfer.create(new CreateMoneyTransfer(transferDetails, transferId))

        then:
        thrown(IllegalArgumentException)
    }

    def "can't create money transfer using the same account for debit and credit"() {
        setup:
        def transferDetails = new TransferDetails.builder()
                .toAccountId(toAccountId)
                .fromAccountId(toAccountId)
                .amount(ONE_HUNDRED_BUCKS)
                .build()

        when:
        transfer.create(new CreateMoneyTransfer(transferDetails, transferId))

        then:
        thrown(IllegalArgumentException)
    }

    def "money transfer records debit fact"() {
        setup:
        def transfer = created(transferId, detailsWithTransferAmount(ONE_HUNDRED_BUCKS))

        when:
        transfer.recordDebit()

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == DebitRecorded
        and:
        event.transferId == transferId
    }

    def "money transfer records credit fact"() {
        setup:
        def transfer = created(transferId, detailsWithTransferAmount(ONE_HUNDRED_BUCKS))

        when:
        transfer.recordCredit()

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == CreditRecorded
        and:
        event.transferId == transferId
    }

    def "money transfer records failed debit fact"() {
        setup:
        def transfer = created(transferId, detailsWithTransferAmount(ONE_HUNDRED_BUCKS))

        when:
        transfer.recordFailedDebit()

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == FailedDebitRecorded
        and:
        event.transferId == transferId
    }

    def "money transfer records fact of not found account"() {
        setup:
        def transfer = created(transferId, detailsWithTransferAmount(ONE_HUNDRED_BUCKS))

        when:
        transfer.recordNotFoundAccount(new RecordNotFoundAccount(transferId, toAccountId))

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == AccountNotFound
        and:
        event.transferId == transferId
        and:
        event.notFoundAccountId == toAccountId
    }

    private detailsWithTransferAmount(Money transferAmount) {
        new TransferDetails.builder()
                .toAccountId(toAccountId)
                .fromAccountId(fromAccountId)
                .amount(transferAmount)
                .build()
    }


}
