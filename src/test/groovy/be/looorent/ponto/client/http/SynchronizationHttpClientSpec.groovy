package be.looorent.ponto.client.http

import be.looorent.ponto.synchronization.ResourceNotFoundException
import spock.lang.Specification

import static be.looorent.ponto.synchronization.ResourceType.ACCOUNT
import static be.looorent.ponto.synchronization.SynchronizationStatus.PENDING
import static be.looorent.ponto.synchronization.SynchronizationSubtype.ACCOUNT_TRANSACTIONS

class SynchronizationHttpClientSpec extends Specification implements RestTrait {

    private static final String BE43105046761783_ID = "01e5fc5c-2b94-449b-ad53-8a49526f6a5c"
    private static final String BE76529131222036_ID = "32ae85e3-83f2-4057-966b-1f96285a3456"

    def client = new SynchronizationHttpClient(http)

    def """
        #find(null)
        throws an Exception
        """() {
        when:
        client.find(null)

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #find(unknown synchronization id )
        returns empty
        """() {
        when:
        def synchronizationFound = client.find(unkwownSynchronizationId)

        then:
        !synchronizationFound.isPresent()

        where:
        unkwownSynchronizationId                                | _
        UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2") | _
        UUID.fromString("e104af4a-c82f-42af-9eae-b2c30b92c0cb") | _
        UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91") | _
    }

    def """
        #find(valid sync id)
        returns the synchronization finished
        """() {
        given:
        def originalSynchronization = client.synchronizeAccountDetails(existingAccountId)
        sleep(2000)

        when:
        def synchronizationFound = client.find(originalSynchronization.id)

        then:
        synchronizationFound.isPresent()
        with(synchronizationFound.get()) {
            id == originalSynchronization.id
            subtype == originalSynchronization.subtype
            resourceType == originalSynchronization.resourceType
            resourceId == originalSynchronization.resourceId
            status != null
            status != PENDING
            updatedAt.isAfter(createdAt)
        }

        where:
        existingAccountId                    | _
        UUID.fromString(BE43105046761783_ID) | _
        UUID.fromString(BE76529131222036_ID) | _
    }

    def """
        #synchronizeAccountTransactions(null)
        return empty
        """() {
        when:
        client.synchronizeAccountTransactions(null)

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #synchronizeAccountTransactions(unkwnown account)
        throws an Exception
        """() {
        when:
        client.synchronizeAccountTransactions(unkwownAccountId)

        then:
        def e = thrown(ResourceNotFoundException)
        e.resourceId == unkwownAccountId
        e.cause instanceof HttpClientException

        where:
        unkwownAccountId                                        | _
        UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2") | _
        UUID.fromString("e104af4a-c82f-42af-9eae-b2c30b92c0cb") | _
        UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91") | _
        UUID.fromString("3c1c5485-ca35-4d7c-992d-d185f15a8b01") | _
        UUID.fromString("50509e65-3e02-4593-ab95-4a1d888eddc8") | _
    }

    def """
        #synchronizeAccountTransactions(knownAccount)
        returns a synchronization
        """() {
        when:
        def synchronization = client.synchronizeAccountTransactions(existingAccountId)

        then:
        synchronization != null
        synchronization.resourceType == ACCOUNT
        synchronization.status == PENDING
        synchronization.subtype == ACCOUNT_TRANSACTIONS
        synchronization.resourceId == existingAccountId

        where:
        existingAccountId                    | _
        UUID.fromString(BE43105046761783_ID) | _
        UUID.fromString(BE76529131222036_ID) | _
    }

    def """
        #synchronizeAccountDetails(null)
        throws an Exception
        """() {
        when:
        client.synchronizeAccountDetails(null)

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #synchronizeAccountDetails(unkwnown account)
        throws an Exception
        """() {
        when:
        client.synchronizeAccountDetails(unkwownAccountId)

        then:
        def e = thrown(ResourceNotFoundException)
        e.resourceId == unkwownAccountId
        e.cause instanceof HttpClientException

        where:
        unkwownAccountId                                        | _
        UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2") | _
        UUID.fromString("e104af4a-c82f-42af-9eae-b2c30b92c0cb") | _
        UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91") | _
        UUID.fromString("3c1c5485-ca35-4d7c-992d-d185f15a8b01") | _
        UUID.fromString("50509e65-3e02-4593-ab95-4a1d888eddc8") | _
    }
}
