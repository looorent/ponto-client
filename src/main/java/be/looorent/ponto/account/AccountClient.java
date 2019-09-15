package be.looorent.ponto.account;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;

import java.util.Optional;
import java.util.UUID;

public interface AccountClient {

    Optional<Account> find(UUID id);
    CollectionResponse<Account> findAll();
    CollectionResponse<Account> findAll(Page page);

}
