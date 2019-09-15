package be.looorent.ponto.client.http;

import lombok.Value;

import java.util.Collection;

@Value
class HttpErrors {
    Collection<HttpError> errors;

    @Value
    static class HttpError {
        String code;
        String detail;
    }
}
