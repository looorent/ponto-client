package be.looorent.ponto.client;

import lombok.NonNull;
import lombok.Value;

import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

@Value
public class Configuration {

    private static final String DEFAULT_URL = "https://api.myponto.com";
    private static final int DEFAULT_TIMEOUT_IN_MS = 10000;

    @NonNull private final String url;

    /**
     * @return Your Ponto API token.
     */
    @NonNull private final Token token;

    /**
     * @return When calling a client method, maximal duration (in milliseconds) before the HTTP client throws a TimeoutException
     */
    private final int timeoutInMillis;

    public static Configuration of(String url,
                                   String token,
                                   String timeoutInMillis) {
        return new Configuration(ofNullable(url)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .orElse(DEFAULT_URL),
                new Token(token),
                ofNullable(timeoutInMillis).map(Integer::parseInt).orElse(DEFAULT_TIMEOUT_IN_MS));
    }

    public static Configuration fromEnvironmentVariables() {
        return of(
                getenv("PONTO_URL"),
                getenv("PONTO_TOKEN"),
                getenv("PONTO_TIMEOUT_IN_MS")
        );
    }

    public static Configuration fromProperties(Properties properties) {
        return of(
                properties.getProperty("ponto.url"),
                properties.getProperty("ponto.token"),
                properties.getProperty("ponto.timeoutInMs")
        );
    }

    public Configuration withToken(String token) {
        return new Configuration(url, new Token(token), timeoutInMillis);
    }
}
