## Motivation

The main aim of this project is to get a hands-on experience with *Event Sourcing* and *CQRS* 

## Build and Run

1. Clone with `git clone https://github.com/pomkine/money-transfers`
2. Build a *jar* with `./gradlew shadowJar`
3. Run the *jar* with `java -jar build/libs/money-transfers.jar server config.yml`

## API Reference

- *Open account* - **POST** `/account`
   - *Payload*: ```{   
                   "initial_balance": {amount}   
                  }   
   				```
   - *Example*:```{
                   "initial_balance": 125.6
                 }
   				```
   
- *Get Account information* - **GET** `/account/{account_id}`
- *Transfer money* - **POST** `/transfer`
   - *Payload*: ```{
                 	"from_account":"{from_account_id}",
                 	"to_account":"{to_account_id}",
                 	"amount":{amount}
                 }```
   - *Example*: ```{
                 	"from_account":"32287c81-e925-466e-9753-1de9e2184a57",
                 	"to_account":"6a61adb1-d566-4f10-804f-95ed6e8fa152",
                 	"amount":6.55
                 }```
                 
## Domain model

- [**Account**](../master/src/main/java/com/pomkine/domain/account/Account.java) aggregate
    - **Commands**
       - Open account
       - Credit account
       - Debit account
    - **Events**
       - Account opened
       - Account credited
       - Account debited
       - Account debit failed due to insufficient funds

- [**Money transfer**](../master/src/main/java/com/pomkine/domain/transfer/MoneyTransfer.java) aggregate
   - **Commands**
      - Create money transfer
      - Record not found account
   - **Events**
      - Money transfer created
      - Account not found
      - Credit recorded
      - Debit recorded
      - Failed debit recorded

## Tests

- [Domain model tests](../master/src/test/groovy/com/pomkine/domain)
- [Acceptance tests](../master/src/test/groovy/com/pomkine/MoneyTransferAcceptanceSpec.groovy)

## Built with
- [Dropwizard](https://www.dropwizard.io)
- [Guice](https://github.com/google/guice)

- [Spock](http://spockframework.org/)
- [REST-assured](http://rest-assured.io/)
- [Gradle](https://gradle.org/)

## TO DO
1. Gradle config to run unit and acceptance tests separately
2. Transfers read model
3. Commands retry on version conflict
