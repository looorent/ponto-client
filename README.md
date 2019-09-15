[![Build Status](https://travis-ci.org/looorent/ponto-client.svg?branch=master)](https://travis-ci.org/looorent/ponto-client)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/be.looorent/ponto-client/badge.svg)](http://search.maven.org/#artifactdetails%7Cbe.looorent%7Cponto-client)

# Ponto Java Client Library

This library helps JVM-lovers to use the [Rest API of Ponto](https://documentation.myponto.com/api).
Hint: this is a draft. :)

# Getting Started

## Compatibility

Requires:
* Java 8+
* A valid token from Ponto

Tested with:
* JDKs 8, 9, 10, 11, 12

## Where can I get the latest release?

* You can pull it from Gradle by adding this line to your `dependencies`:
```groovy
implementation "be.looorent:ponto-client:0.0.1"
```

* or with Maven in your `pom.xml`:
```xml
<dependency>
    <groupId>be.looorent</groupId>
    <artifactId>ponto-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Logging

This library uses log4j with the prefix `be.looorent.ponto`.

## Configuration

An instance of `be.looorent.ponto.client.Configuration` defines how the Ponto client is configured. This object can be instanciated:
* programmatically using `Configuration.of`;
* from an instance of `java.util.Properties` using `Configuration.fromProperties`;
* from the environment variables using `Configuration.fromEnvironmentVariables`.

| Option | Environment Variable | Property | Default Value | Type | Required? | Description |
| ---- | ----- | ----- | ----- | ------ | ----- | ------ |
| `token` | `PONTO_TOKEN` | `ponto.token` | `null` | String | Required | Your Ponto API token. | 
| `url` | `PONTO_URL` | `ponto.url` | `https://api.myponto.com`| String | Optional | The base url the Ponto API is located. | 
| `timeoutInMillis` | `PONTO_TIMEOUT_IN_MS` | `ponto.timeoutInMs` | `10000`| Integer | Optional | When calling a client method, maximal duration (in milliseconds) before the HTTP client throws a TimeoutException. | 

## Get a Client instance

```java
import be.looorent.ponto.client.PontoClient;
import be.looorent.ponto.client.http.PontoHttpClient;

PontoClient ponto = PontoHttpClient.from(configuration);
```

## Paging

This client helps to define pages

## Example: List two financial institutions

```java
import be.looorent.ponto.institution.FinancialInstitution;
import be.looorent.ponto.client.CollectionResponse;

Page page = Page.builder().limit(2).build();
CollectionResponse<FinancialInstitution> institutions = ponto.financialInstitutions().findAll(page);
```

## Java Example: Get an account by ID

```java
var id = UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2");
ponto.accounts().find(id)
```

## Java Example: Synchronize all the transactions for a given account and wait for the synchronization to complete

```Java
var id = UUID.fromString("01e5fc5c-2b94-449b-ad53-8a49526f6a5c");
var client = ponto.synchronizations()
var synchronization = client.synchronizeAccountTransactions(id);
while (!synchronization.isComplete()) {
    sleep(250);
    synchronization = client.find(synchronization.getId())
            .orElseThrow(() -> new IllegalStateException("Synchronization disappeared ?"));
}
```

## Groovy Example: Fetch all transactions of a given account

```groovy
def accountId         = UUID.fromString("01e5fc5c-2b94-449b-ad53-8a49526f6a5c")
def transactionClient = client.transactions()
def response          = transactionClient.findAll(accountId)
def transactions      = [] + response.entities
while (response.hasAfter()) {
    response      = transactionClient.findAll(accountId, response.page.next())
    transactions += response.entities
}
```

# Development

Environment of development:
* AdoptOpenJDK 8
* Gradle wrapper

## Technical choices

* The fewest dependencies as possible. This means: no `HttpClient`, no `jsonapi` library, no `commons`,...
* `PontoClient`'s interface is very simple and absolutely not fluent. A fluent interface could be written on top of it (but this is another interface).
* All data structures are immutable.
* It would be really nice to use the JDK 11 Http Client but Java 8 is still more popular. Compatibility matters.

## Running tests locally

### Requirements

All the integrations tests are based on a specific Ponto Sandbox.
They will fail if you do not configure your sandbox correctly.

What you need:
* A Ponto Token
* Your Ponto sandbox configured like this:
    * `MyTestAccount` enabled with *two* accounts: `IBAN BE76 5291 3122 2036` and `IBAN BE95 5465 4940 7322`
    * `Gringotts Bank` enabled with *a single* account: `IBAN BE43 1050 4676 1783`
   
A personal Token is already defined in the `build.gradle` configuration. However, this token should be removed eventually.

### Running all tests

```shell
./gradlew test
```
 
## How to deploy a new version to Maven central

Following this [great article](http://nemerosa.ghost.io/2015/07/01/publishing-to-the-maven-central-using-gradle/), you should configure your `./gradle/gradle.propreties` file and then:

```
$ ./gradlew -Prelease uploadArchives closeAndPromoteRepository
```

## Future work

* Make the HTTP client more configurable (proxies, ...)
* Fluent Interface?
* More tests