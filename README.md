## Motivation

The main aim of this project is to get a hands-on experience with *Event Sourcing* and *CQRS* 

## Build and Run

1. Clone with `git clone https://github.com/pomkine/money-transfers`
2. Build a runnable *jar* with `./gradlew shadowJar`
3. Run the *jar* with `java -jar build/libs/money-transfers.jar server`

## API Reference

- *Open account* - **POST** `/account`
   *Payload*: ```{
                   "initial_balance": 1129.5
                 }
   				```
- *Get Account information* - **GET** `/account/{account_id}`
- *Transfer money* - **POST** `/transfer`
   *Payload*: ```{
                 	"from_account":"32287c81-e925-466e-9753-1de9e2184a57",
                 	"to_account":"6a61adb1-d566-4f10-804f-95ed6e8fa152",
                 	"amount":3
                 }```

## Tests

- [Domain model tests](../blob/master/src/test/groovy/com/pomkine/domain)
- [Acceptance tests](../blob/master/src/test/groovy/com/pomkine/MoneyTransferAcceptanceSpec.groovy)

## Built with
- [Dropwizard](https://www.dropwizard.io)
- [Guice](https://github.com/google/guice)

- [Spock](http://spockframework.org/)
- [REST-assured](http://rest-assured.io/)
- [Gradle](https://gradle.org/)

## TO DO
1. Event stream optimistic locking
2. Gradle config to run unit and acceptance tests separately
3. Transfers read model
