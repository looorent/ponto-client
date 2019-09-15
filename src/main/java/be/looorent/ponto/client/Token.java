package be.looorent.ponto.client;

import static java.util.Optional.ofNullable;

public class Token {

    private final String token;

    Token(String token) {
        this.token = ofNullable(token)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("When authenticating, a token must not be null or empty."));
    }

    public String getToken() {
        return token;
    }

    public String asBearerHeader() {
        return "Bearer "+token;
    }
}
