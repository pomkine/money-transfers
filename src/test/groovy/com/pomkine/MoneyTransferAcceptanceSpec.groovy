package com.pomkine

import com.pomkine.application.MoneyTransferApplication
import com.pomkine.application.MoneyTransferConfiguration
import io.dropwizard.testing.DropwizardTestSupport
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue

class MoneyTransferAcceptanceSpec extends Specification {

    static final DropwizardTestSupport<MoneyTransferConfiguration> SUPPORT =
            new DropwizardTestSupport<MoneyTransferConfiguration>(
                    MoneyTransferApplication.class, new MoneyTransferConfiguration())

    def setupSpec() {
        SUPPORT.before()
    }

    def cleanupSpec() {
        SUPPORT.after()
    }

    def "account create call should return created account id"() {
        setup:
        def request = given()
                .contentType(ContentType.JSON)
                .log().all()
        when:
        def response = request.body("{\n" +
                "  \"initial_balance\": 1129.5\n" +
                "}").with()
                .post("/account")
        then:
        response.then().log().all()
                .statusCode(200)
                .body("account_id", notNullValue())
    }

    def "get account info should return requested account info if found"() {
        setup:
        def initialBalance = Money.of(CurrencyUnit.USD, BigDecimal.TEN)
        def accountId = createAccount(initialBalance)
        def request = given()
                .contentType(ContentType.JSON)
                .log().all()

        when:
        def response = request.get("/account/{id}", accountId)

        then:
        response.then().log().all()
                .statusCode(200)
                .body("account_id", notNullValue())
                .body("balance.amount", equalTo(initialBalance.getAmount().toFloat()))
    }

    def "get account info should return 404 if not found"() {
        setup:
        def request = given()
                .contentType(ContentType.JSON)
                .log().all()

        when:
        def accountInfoResp = getAccountInfo("some_account_id")

        then:
        accountInfoResp.then().log().all()
                .statusCode(404)
    }

    def "transfer money should debit one account and credit another"() {

        setup:
        def account1Balance = Money.parse("USD 200")
        def account2Balance = Money.parse("USD 500")
        def account1 = createAccount(account1Balance)
        def account2 = createAccount(account2Balance)
        def toTransfer = Money.parse("USD 5")

        when:
        def transferResp = transferMoney(account1, account2, toTransfer)

        then:
        transferResp.then().log().all()
                .statusCode(200)
                .body("transfer_id", notNullValue())

        def account1InfoResp = getAccountInfo(account1)
        account1InfoResp.then().log().all()
                .body("balance.amount",
                equalTo((account1Balance - toTransfer).getAmount().toFloat()))

        def account2InfoResp = getAccountInfo(account2)
        account2InfoResp.then().log().all()
                .body("balance.amount",
                equalTo((account2Balance + toTransfer).getAmount().toFloat()))
    }

    private Response transferMoney(String from, String to, Money amount) {
        return given().contentType(ContentType.JSON)
                .log().all()
                .body(
                "{\"from_account\":\"${from}\",\"to_account\":\"${to}\"," +
                        "\"amount\":${amount.getAmount()}}")
                .with().post("/transfer")
    }

    private Response getAccountInfo(String accountId) {
        return given().contentType(ContentType.JSON)
                .with().get("/account/{id}", accountId)
    }


    private String createAccount(Money initialBalance) {
        def createAccountResponse = given().contentType(ContentType.JSON)
                .body("{\"initial_balance\":${initialBalance.getAmount()}}")
                .with().post("/account")
        return createAccountResponse.body().path("account_id")
    }


}
