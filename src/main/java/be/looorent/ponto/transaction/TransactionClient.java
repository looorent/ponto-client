package be.looorent.ponto.transaction;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;

import java.util.Optional;
import java.util.UUID;

public interface TransactionClient {
    Optional<Transaction> find(UUID accountId, UUID transactionId);
    CollectionResponse<Transaction> findAll(UUID accountId);
    CollectionResponse<Transaction> findAll(UUID accountId, Page page);
}
