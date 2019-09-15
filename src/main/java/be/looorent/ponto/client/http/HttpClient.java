package be.looorent.ponto.client.http;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Configuration;
import be.looorent.ponto.client.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static be.looorent.ponto.client.http.PageUtil.toQueryString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Slf4j
class HttpClient {

    private static final String GET = "GET";
    private static final String POST = "GET";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT = "Accept";
    private static final String AUTHORIZATION = "Authorization";

    private final Configuration configuration;
    private final ObjectMapper mapper;

    HttpClient(@NonNull Configuration configuration, @NonNull ObjectMapper mapper) {
        this.configuration = configuration;
        this.mapper = mapper;
    }

    public <E, M extends JsonMapping<E>> CollectionResponse<E> list(String path,
                                                                    Page page,
                                                                    Class<M> mapping) {
        try {
            JsonApi.Collection<M> response = internalList(path, page, mapping);
            return new JsonApi.Collection<>(
                    response.getMeta(),
                    response.getLinks(),
                    response.getData().stream().map(JsonMapping::toEntity).collect(toList())
            );
        } catch (IOException e) {
            LOG.error("An error occurred when accessing {}.", path, e);
            throw new HttpClientException(e);
        }
    }

    protected <M extends JsonMapping<?>> JsonApi.Collection<M> internalList(String path,
                                                                            Page page,
                                                                            Class<M> mapping) throws IOException {
        var url = getIndexUrl(path, page);
        var connection = createGetConnection(url);
        connection.setConnectTimeout(configuration.getTimeoutInMillis());

        try {
            connection.connect();
            @Cleanup var reader = new InputStreamReader(connection.getInputStream(), UTF_8);
            @Cleanup var buffer = new BufferedReader(reader);
            LOG.trace("GET {} responded with 200", connection.getURL());
            var type = mapper.getTypeFactory().constructParametricType(JsonApi.Collection.class, mapping);
            return mapper.readValue(buffer, type);
        } catch (IOException e) {
            throw createException(url, connection);
        } finally {
            connection.disconnect();
        }
    }

    public <B, R, M extends JsonMapping<R>> R post(String path,
                                                   JsonApi.Single<B> body,
                                                   Class<M> responseMapping) {
        try {
            return internalPost(path, body, responseMapping).toEntity();
        } catch (IOException e) {
            LOG.error("An error occurred when posting at {}.", path, e);
            throw new HttpClientException(e);
        }
    }

    protected <B, M extends JsonMapping<?>> M internalPost(String path,
                                                           JsonApi.Single<B> body,
                                                           Class<M> responseMapping) throws IOException {
        var url = postUrl(path);
        var connection = createPostConnection(url);

        try {
            @Cleanup var payloadStream = connection.getOutputStream();
            mapper.writeValue(payloadStream, body);
            connection.connect();

            @Cleanup var reader = new InputStreamReader(connection.getInputStream(), UTF_8);
            @Cleanup var buffer = new BufferedReader(reader);

            LOG.trace("GET {} responded with 200", connection.getURL());
            var type = mapper.getTypeFactory().constructParametricType(JsonApi.Single.class, responseMapping);
            JsonApi.Single<M> response = mapper.readValue(buffer, type);
            return response.getData();
        } catch (IOException e) {
            throw createException(url, connection);
        }
    }

    public <E, M extends JsonMapping<E>>  Optional<E> get(String path,
                                                          UUID id,
                                                          Class<M> mapping) {
        try {
            return internalGet(path, id, mapping)
                    .map(JsonApi.Single::getData)
                    .map(JsonMapping::toEntity);
        } catch (IOException e) {
            LOG.error("An error occurred when accessing {}.", path, e);
            throw new HttpClientException(e);
        }
    }

    protected <M extends JsonMapping<?>> Optional<JsonApi.Single<M>> internalGet(String path,
                                                                                 UUID id,
                                                                                 Class<M> mapping) throws IOException {
        var url = getShowUrl(path, id);
        var connection = createGetConnection(url);

        try {
            connection.connect();
            @Cleanup var reader = new InputStreamReader(connection.getInputStream(), UTF_8);
            @Cleanup var buffer = new BufferedReader(reader);
            LOG.trace("GET {} responded with 200", connection.getURL());
            var type = mapper.getTypeFactory().constructParametricType(JsonApi.Single.class, mapping);
            return of(mapper.readValue(buffer, type));
        } catch (FileNotFoundException e) {
            LOG.trace("GET {} responded with {}", connection.getURL(), connection.getResponseCode());
            return empty();
        } catch (IOException e) {
            throw createException(url, connection);
        }
    }

    private HttpClientException createException(URL url, HttpURLConnection connection) throws IOException {
        @Cleanup var reader = new InputStreamReader(connection.getErrorStream(), UTF_8);
        @Cleanup var buffer = new BufferedReader(reader);
        var errors = mapper.readValue(buffer, HttpErrors.class);
        var responseCode = connection.getResponseCode();
        LOG.error("GET {} responded with code {}: {}", url, responseCode, errors);
        return new HttpClientException(url, responseCode, errors);
    }

    private HttpURLConnection createGetConnection(URL url) throws IOException {
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(GET);
        connection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        connection.setRequestProperty(AUTHORIZATION, configuration.getToken().asBearerHeader());
        connection.setConnectTimeout(configuration.getTimeoutInMillis());
        connection.setReadTimeout(configuration.getTimeoutInMillis());
        return connection;
    }

    private HttpURLConnection createPostConnection(URL url) throws IOException {
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(POST);
        connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        connection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        connection.setRequestProperty(AUTHORIZATION, configuration.getToken().asBearerHeader());
        connection.setConnectTimeout(configuration.getTimeoutInMillis());
        connection.setDoOutput(true);
        return connection;
    }

    private URL getIndexUrl(String path, Page page) throws MalformedURLException {
        return new URL(configuration.getUrl() + path + toQueryString(page));
    }

    private URL getShowUrl(String path, UUID id) throws MalformedURLException {
        return new URL(configuration.getUrl() + path + "/" + id.toString());
    }

    private URL postUrl(String path) throws MalformedURLException {
        return new URL(configuration.getUrl() + path);
    }
}
