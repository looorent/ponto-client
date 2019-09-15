package be.looorent.ponto.client.http;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;
import be.looorent.ponto.transaction.Transaction;
import be.looorent.ponto.transaction.TransactionClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static be.looorent.ponto.client.Page.DEFAULT_PAGE;

@Slf4j
class TransactionHttpClient implements TransactionClient {
    private static final String ACCOUNT_PATH = "/accounts";
    private static final String TRANSACTION_PATH = "/transactions";

    private final HttpClient http;

    TransactionHttpClient(@NonNull HttpClient http) {
        this.http = http;
    }

    @Override
    public Optional<Transaction> find(@NonNull UUID accountId, @NonNull UUID transactionId) {
        return http.get(transactionPath(accountId), transactionId, TransactionMapping.Data.class);
    }

    @Override
    public CollectionResponse<Transaction> findAll(@NonNull UUID accountId) {
        return findAll(accountId, DEFAULT_PAGE);
    }

    // TODO tests
    @Override
    public CollectionResponse<Transaction> findAll(@NonNull UUID accountId, Page page) {
        if (page == null) {
            return findAll(accountId);
        } else {
            return http.list(transactionPath(accountId), page, TransactionMapping.Data.class);
        }
    }

    private String transactionPath(UUID accountId) {
        return ACCOUNT_PATH + "/" + accountId + TRANSACTION_PATH;
    }
}
