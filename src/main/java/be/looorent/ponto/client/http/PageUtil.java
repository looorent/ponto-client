package be.looorent.ponto.client.http;

import be.looorent.ponto.client.Page;
import lombok.var;

import java.util.ArrayList;
import java.util.Collection;

class PageUtil {
    static String toQueryString(Page page) {
        var array = toQueryArray(page);
        if (array.isEmpty()) {
            return "";
        } else {
            return "?"+ String.join("&", array);
        }
    }

    private static Collection<String> toQueryArray(Page page) {
        var query = new ArrayList<String>();
        if (page != null) {
            if (page.hasLimit()) {
                query.add("limit="+page.getLimit());
            }
            if (page.hasBefore()) {
                query.add("before="+page.getBefore());
            }
            if (page.hasAfter()) {
                query.add("after="+page.getAfter());
            }
        }
        return query;
    }
}
