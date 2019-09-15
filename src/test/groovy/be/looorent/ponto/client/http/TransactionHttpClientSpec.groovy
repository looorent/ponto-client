package be.looorent.ponto.client.http


import spock.lang.Specification

import static be.looorent.ponto.transaction.RemittanceInformationType.UNSTRUCTURED

class TransactionHttpClientSpec extends Specification implements RestTrait {

    private static final UUID BE43105046761783_ID = UUID.fromString("01e5fc5c-2b94-449b-ad53-8a49526f6a5c")
    private static final UUID BE76529131222036_ID = UUID.fromString("32ae85e3-83f2-4057-966b-1f96285a3456")

    private static final UUID TRANSACTION_IN_BE43 = UUID.fromString("824bd430-1d94-495b-bba4-61c36815e21a")
    private static final UUID TRANSACTION_IN_BE76 = UUID.fromString("e34ce08e-0cbc-4d02-9788-3512dc4be89a")

    def client = new TransactionHttpClient(http)

    def """
        #find(null, null)
        throws an Exception
        """() {
        when:
        client.find(accountId, transactionId)

        then:
        thrown(IllegalArgumentException)

        where:
        accountId           | transactionId
        BE43105046761783_ID | null
        null                | TRANSACTION_IN_BE76
        null                | null
    }

    def """
        #find(unknown account id, known transactionId)
        returns empty
        """() {
        when:
        def transactionFound = client.find(unkwnownAccountId, knownTransactionId)

        then:
        !transactionFound.isPresent()

        where:
        unkwnownAccountId                                       | knownTransactionId
        UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2") | TRANSACTION_IN_BE43
        UUID.fromString("e104af4a-c82f-42af-9eae-b2c30b92c0cb") | TRANSACTION_IN_BE43
        UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91") | TRANSACTION_IN_BE43
    }

    def """
        #find(known account id, unknownTransactionId)
        returns empty
        """() {
        when:
        def transactionFound = client.find(existingAccountId, unknownTransactionId)

        then:
        !transactionFound.isPresent()

        where:
        existingAccountId   | unknownTransactionId
        BE43105046761783_ID | UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2")
        BE76529131222036_ID | UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91")
    }

    def """
        #find(known account id, knownTransactionId) but with a mismatch
        returns empty
        """() {
        when:
        def transactionFound = client.find(existingAccountId, wrongTransactionId)

        then:
        !transactionFound.isPresent()

        where:
        existingAccountId   | wrongTransactionId
        BE43105046761783_ID | TRANSACTION_IN_BE76
        BE76529131222036_ID | TRANSACTION_IN_BE43
    }

    def """
        #find(known account id, knownTransactionId)
        returns empty
        """() {
        when:
        def transactionFound = client.find(existingAccountId, knownTransactionId)

        then:
        transactionFound.isPresent()
        with(transactionFound.get()) {
            id == knownTransactionId
            valueDate != null
            remittanceInformationType == UNSTRUCTURED
            remittanceInformation != null
            executionDate != null
            currency != null
            counterpartReference != null
            counterpartName != null
            amount != null
            accountId == existingAccountId
        }

        where:
        existingAccountId   | knownTransactionId
        BE43105046761783_ID | TRANSACTION_IN_BE43
        BE76529131222036_ID | TRANSACTION_IN_BE76
    }
}
