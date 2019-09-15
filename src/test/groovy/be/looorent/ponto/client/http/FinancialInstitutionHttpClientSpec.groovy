package be.looorent.ponto.client.http

import be.looorent.ponto.client.Page
import spock.lang.Specification

class FinancialInstitutionHttpClientSpec extends Specification implements RestTrait {

    private static final UUID MY_TEST_BANK_ID = UUID.fromString("1c6dd8e0-e558-4bae-b4ed-2c52060ccea9")
    private static final URL ENDPOINT_URL = new URL("https://api.myponto.com/financial-institutions")
    private static final String MY_TEST_BANK_NAME = "MyTestBank"
    private static final UUID GRINGOTTS_ID = UUID.fromString("702f98c1-773b-4365-afc6-369f91b6824a")
    private static final String GRINGOTTS_NAME = "Gringotts Bank"

    def client = new FinancialInstitutionHttpClient(http)

    def """
        #findAll(no page)
        returns:
            - entities: 'MyTestBank' and 'Gringotts Bank'
            - links: first only
            - page: limit=10 only
        """() {
        when:
        def response = client.findAll()

        then:
        with(response.page) {
            limit == 10
            before == null
            after == null
        }

        with(((JsonApi.Collection) response).links) {
            first == ENDPOINT_URL
            prev == null
            next == null
        }

        response.entities.size() == 2
        with(response.entities.first()) {
            id == MY_TEST_BANK_ID
            name == MY_TEST_BANK_NAME
        }

        with(response.entities[1]) {
            id == GRINGOTTS_ID
            name == GRINGOTTS_NAME
        }
    }

    def """
        #findAll(large page)
        returns:
            - entities: 'MyTestBank' and 'Gringotts Bank'
            - links: first only
            - page: limit=10 only
        """() {
        when:
        def page = Page.builder().limit(limit).build()
        def response = client.findAll(page)

        then:
        with(response.page) {
            limit == limit
            before == null
            after == null
        }

        with(((JsonApi.Collection) response).links) {
            first == new URL("${ENDPOINT_URL}?limit=${limit}")
            prev == null
            next == null
        }

        response.entities.size() == 2
        with(response.entities.first()) {
            id == MY_TEST_BANK_ID
            name == MY_TEST_BANK_NAME
        }

        with(response.entities[1]) {
            id == GRINGOTTS_ID
            name == GRINGOTTS_NAME
        }

        where:
        limit | _
        11    | _
        20    | _
        5     | _
    }

    def """
        #findAll(limit=1, before=null, after=null)
        returns:
            - entities: 'MyTestBank' only
            - links: next and first
            - page: limit=1 and after
        """() {
        given:
        def page = Page.builder().limit(1).build()

        when:
        def response = client.findAll(page)

        then:
        with(response.page) {
            limit == 1
            before == null
            after == MY_TEST_BANK_ID
        }

        with(((JsonApi.Collection) response).links) {
            first == new URL("${ENDPOINT_URL}?limit=1")
            prev == null
            next == new URL("${ENDPOINT_URL}?after=${MY_TEST_BANK_ID}&limit=1")
        }

        response.entities.size() == 1
        with(response.entities.first()) {
            id == MY_TEST_BANK_ID
            name == MY_TEST_BANK_NAME
        }
    }

    def """
        #findAll(invalid limit, before=null, after=null)
        throws an exception
        """() {
        given:
        def page = Page.builder().limit(invalidLimit).build()

        when:
        client.findAll(page)

        then:
        HttpClientException e = thrown()
        e.errors.size() == 1
        with(e.errors.first()) {
            code == "invalid_paging_limit"
        }

        where:
        invalidLimit | _
        101          | _
        1001         | _
        -1           | _
        10000000     | _
        0            | _
    }

    def """
        #findAll() with an invalid token
        throws an exception
        """() {
        given:
        def wrongConfiguration = configuration.withToken(invalidToken)
        def invalidClient = new FinancialInstitutionHttpClient(new HttpClient(wrongConfiguration, mapper))

        when:
        invalidClient.findAll()

        then:
        HttpClientException e = thrown()
        e.errors.size() == 1
        with(e.errors.first()) {
            code == "authorizationCodeInvalid"
        }

        where:
        invalidToken | _
        "abc"        | _
        "----"       | _
        "xYz"        | _
        "_____"      | _
    }

    def """
        #findAll(limit=null, before=null, after=<gringott's id>)
        returns:
            - entities: []
            - links: first only
            - page: limit=10
        """() {
        given:
        def page = Page.builder().after(GRINGOTTS_ID).build()

        when:
        def response = client.findAll(page)

        then:
        with(response.page) {
            limit == 10
            before == null
            after == null
        }

        with(((JsonApi.Collection) response).links) {
            first == ENDPOINT_URL
            prev == null
            next == null
        }

        response.entities.isEmpty()
    }

    def """
        #findAll(limit=2, before=null, after=<mytestbank's id>)
        returns:
            - entities: [gringott]
            - links: first and prev (mytestbank)
            - page: limit=2 and before (gringotts)
        """() {
        given:
        def page = Page.builder()
                .after(MY_TEST_BANK_ID)
                .limit(2)
                .build()

        when:
        def response = client.findAll(page)

        then:
        with(response.page) {
            limit == 2
            before == GRINGOTTS_ID
            after == null
        }

        with(((JsonApi.Collection) response).links) {
            first ==  new URL("${ENDPOINT_URL}?limit=2")
            prev == new URL("${ENDPOINT_URL}?before=${GRINGOTTS_ID}&limit=2")
            next == null
        }

        response.entities.size() == 1
        with(response.entities.first()) {
            id == GRINGOTTS_ID
            name == GRINGOTTS_NAME
        }
    }

    def """
        #findAll(before=null, after=<unknown's id>)
        returns:
            - entities: []
            - links: first only
            - page: limit=10 only
        """() {
        given:
        def unkwnownId = UUID.fromString("1c6dd8e0-e558-4bae-b4ed-2c52060ccef9")
        def page = Page.builder()
                .after(unkwnownId)
                .build()

        when:
        def response = client.findAll(page)

        then:
        with(response.page) {
            limit == 10
            before == null
            after == null
        }

        with(((JsonApi.Collection) response).links) {
            first == ENDPOINT_URL
            prev == null
            next == null
        }

        response.entities.isEmpty()
    }


    def """
        #find(unknown id)
        returns: empty
        """() {
        given:
        def id = UUID.fromString(unkwnownId)

        when:
        def response = client.find(id)

        then:
        !response.isPresent()

        where:
        unkwnownId                             | _
        "1f6dd8e0-e558-4bae-b4ed-2c52060ccef9" | _
        "77b19af8-1f41-4f76-a47a-d5204d629c0e" | _
        "2e1f8c8c-01dc-4acc-a7b4-aa4f53ea9224" | _
        "e1e4a926-c2a5-401d-b1f9-2f9c3c3ab832" | _
        "60ae6590-0ff0-445e-8cfa-91575f423607" | _
    }

    def """
        #find(known id)
        returns: the institution
        """() {
        when:
        def response = client.find(id)

        then:
        response.isPresent()
        response.map { institution -> institution.getId() }.orElse(null) == id
        response.map { institution -> institution.getName() }.orElse(null) == expectedName

        where:
        id              | expectedName
        GRINGOTTS_ID    | GRINGOTTS_NAME
        MY_TEST_BANK_ID | MY_TEST_BANK_NAME
    }
}
