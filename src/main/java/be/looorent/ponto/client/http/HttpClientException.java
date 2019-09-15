package be.looorent.ponto.client.http;

import java.net.URL;
import java.util.Collection;

import static java.util.Collections.emptyList;

public class HttpClientException extends RuntimeException {

    private final Collection<HttpErrors.HttpError> errors;

    HttpClientException(Throwable cause) {
        super("An error occurred when calling Ponto's REST API", cause);
        this.errors = emptyList();
    }

    HttpClientException(URL url, int statusCode, HttpErrors errors) {
        super("Url: "+url+"; Response code: "+statusCode);
        this.errors = errors.getErrors();
    }

    public Collection<HttpErrors.HttpError> getErrors() {
        return errors;
    }
}
