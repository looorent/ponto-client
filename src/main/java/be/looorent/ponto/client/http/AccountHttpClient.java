package be.looorent.ponto.client.http;

import be.looorent.ponto.account.Account;
import be.looorent.ponto.account.AccountClient;
import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static be.looorent.ponto.client.Page.DEFAULT_PAGE;

@Slf4j
class AccountHttpClient implements AccountClient {

    private static final String PATH = "/accounts";

    private final HttpClient http;

    AccountHttpClient(@NonNull HttpClient http) {
        this.http = http;
    }

    @Override
    public Optional<Account> find(@NonNull UUID id) {
        return http.get(PATH, id, AccountMapping.Data.class);
    }

    @Override
    public CollectionResponse<Account> findAll() {
        return findAll(DEFAULT_PAGE);
    }

    // TODO tests
    @Override
    public CollectionResponse<Account> findAll(Page page) {
        if (page == null) {
            return findAll();
        } else {
            return http.list(PATH, page, AccountMapping.Data.class);
        }
    }
}
