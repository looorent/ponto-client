package be.looorent.ponto.client;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Page {
    public static final Page DEFAULT_PAGE = new Page(null, null, null);

    private Integer limit;
    private UUID before;
    private UUID after;

    public boolean hasLimit() {
        return limit != null;
    }

    public boolean hasBefore() {
        return before != null;
    }

    public boolean hasAfter() {
        return after != null;
    }

    public Page next() {
        return new Page(limit, null, after);
    }
}
