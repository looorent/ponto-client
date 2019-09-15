package be.looorent.ponto.configuration

import be.looorent.ponto.client.Token
import spock.lang.Specification

class TokenSpec extends Specification {

    def """
        #constructor
        with null
        throws an exception
    """() {
        when:
        new Token(null)

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #constructor
        with an empty value
        throws an exception
    """() {
        when:
        new Token("")

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #constructor
        with an blank value
        throws an exception
    """() {
        when:
        new Token("  ")

        then:
        thrown(IllegalArgumentException)
    }

    def """
        #constructor
        remove leading and traling spaces
    """() {
        when:
        def result = new Token(givenToken)

        then:
        result.token == sanitizedToken

        where:
        givenToken     | sanitizedToken
        " abc "        | "abc"
        " XX yI"       | "XX yI"
        "aerb-sdf er " | "aerb-sdf er"
    }

    def "#asBearerHeader - happy path"() {
        when:
        def result = new Token(givenToken)

        then:
        result.asBearerHeader() == bearer

        where:
        givenToken     | bearer
        " abc "        | "Bearer abc"
        " XX yI"       | "Bearer XX yI"
        "aerb-sdf er " | "Bearer aerb-sdf er"
    }

}
