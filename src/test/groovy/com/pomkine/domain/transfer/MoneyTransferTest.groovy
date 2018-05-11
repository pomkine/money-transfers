package com.pomkine.domain.transfer

import com.pomkine.domain.common.AggregateId
import com.pomkine.domain.transfer.event.MoneyTransferCreated
import org.joda.money.Money
import spock.lang.Specification


class MoneyTransferTest extends Specification {

    def fromAccountId = AggregateId.generate()
    def toAccountId = AggregateId.generate()
    def transferId = AggregateId.generate()
    def ONE_HUNDRED_BUCKS = Money.parse("USD 100")
    def NEGATIVE_MONEY_AMOUNT = Money.parse("USD -200")
    def ZERO_MONEY_AMOUNT = Money.parse("USD 0")

    def "money transfer is created with transfer details"() {
        setup:
        def transfer = new MoneyTransfer()
        def transferDetails = detailsWithTransferAmount(ONE_HUNDRED_BUCKS)

        when:
        transfer.create(transferDetails)

        then:
        def events = transfer.getPendingEvents()
        events.size() == 1
        and:
        def event = events.head()
        event.class == MoneyTransferCreated
        and:
        event.transferDetails == transferDetails
    }

    def "can't create money transfer with negative transfer amount"() {
        setup:
        def transfer = new MoneyTransfer()
        def transferDetails = detailsWithTransferAmount(NEGATIVE_MONEY_AMOUNT)

        when:
        transfer.create(transferDetails)

        then:
        thrown(IllegalArgumentException)
    }

    def "can't create money transfer with zero transfer amount"() {
        setup:
        def transfer = new MoneyTransfer()
        def transferDetails = detailsWithTransferAmount(ZERO_MONEY_AMOUNT)

        when:
        transfer.create(transferDetails)

        then:
        thrown(IllegalArgumentException)
    }

    def "can't create money transfer using the same account for debit and credit"() {
        setup:
        def transfer = new MoneyTransfer()
        def transferDetails = new TransferDetails.builder()
                .toAccountId(toAccountId)
                .fromAccountId(toAccountId)
                .transferId(transferId)
                .transferAmount(ONE_HUNDRED_BUCKS)
                .build()

        when:
        transfer.create(transferDetails)

        then:
        thrown(IllegalArgumentException)
    }

    private detailsWithTransferAmount(Money transferAmount) {
        new TransferDetails.builder()
                .toAccountId(toAccountId)
                .fromAccountId(fromAccountId)
                .transferId(transferId)
                .transferAmount(transferAmount)
                .build()
    }


}
