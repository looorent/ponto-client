package be.looorent.ponto.client.http


import spock.lang.Specification

class AccountHttpClientSpec extends Specification implements RestTrait {

    private static final UUID BE43105046761783_ID = UUID.fromString("01e5fc5c-2b94-449b-ad53-8a49526f6a5c")
    private static final UUID BE76529131222036_ID = UUID.fromString("32ae85e3-83f2-4057-966b-1f96285a3456")

    def client = new AccountHttpClient (http)

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
        #find(unknown account id )
        returns empty
        """() {
        when:
        def accountFound = client.find(unkwnownAccountId)

        then:
        !accountFound.isPresent()

        where:
        unkwnownAccountId                                       | _
        UUID.fromString("b3f481ff-a7bb-4220-8525-61a36b1dc4d2") | _
        UUID.fromString("e104af4a-c82f-42af-9eae-b2c30b92c0cb") | _
        UUID.fromString("1ccebc3b-c1e9-402d-be88-c4744757dc91") | _
    }

    def """
        #find(known account id)
        returns a valid account
        """() {
        when:
        def accountFound = client.find(existingAccountId)

        then:
        accountFound.isPresent()

        where:
        existingAccountId   | _
        BE43105046761783_ID | _
        BE76529131222036_ID | _
    }
}
