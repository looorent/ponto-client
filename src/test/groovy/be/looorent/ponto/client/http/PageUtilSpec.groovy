package be.looorent.ponto.client.http

import be.looorent.ponto.client.Page
import spock.lang.Specification

import static be.looorent.ponto.client.http.PageUtil.toQueryString
import static java.util.UUID.randomUUID

class PageUtilSpec extends Specification {

    def "#toQueryString(null) returns ''"() {
        when:
        def queryString = toQueryString(null)

        then:
        queryString == ""
    }

    def "#toQueryString(Page(limit=nil, before=nil, after=nil)) returns ''"() {
        given:
        def emptyPage = new Page(null, null, null)

        when:
        def queryString = toQueryString(emptyPage)

        then:
        queryString == ""
    }

    def "#toQueryString(Page(limit=100, before=nil, after=nil)) returns '?limit=100'"() {
        given:
        def page = new Page(100, null, null)

        when:
        def queryString = toQueryString(page)

        then:
        queryString == "?limit=100"
    }

    def "#toQueryString(Page(limit=nil, before=UUID, after=nil)) returns '?before=UUID'"() {
        given:
        def before = randomUUID()
        def page = new Page(null, before, null)

        when:
        def queryString = toQueryString(page)

        then:
        queryString == "?before=${before}"
    }

    def "#toQueryString(Page(limit=nil, before=nil, after=UUID)) returns '?after=UUID'"() {
        given:
        def after = randomUUID()
        def page = new Page(null, null, after)

        when:
        def queryString = toQueryString(page)

        then:
        queryString == "?after=${after}"
    }

    def "#toQueryString(Page(limit=24, before=UUID, after=UUID)) returns '?limit=24&before=UUID&after=UUID'"() {
        given:
        def before = randomUUID()
        def after = randomUUID()
        def page = new Page(24, before, after)

        when:
        def queryString = toQueryString(page)

        then:
        queryString == "?limit=24&before=${before}&after=${after}"
    }
}
