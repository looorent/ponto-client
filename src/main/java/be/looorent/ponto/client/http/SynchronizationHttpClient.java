package be.looorent.ponto.client.http;

import be.looorent.ponto.client.http.SynchronizationMapping.NewSynchronization;
import be.looorent.ponto.synchronization.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.Optional;
import java.util.UUID;

import static be.looorent.ponto.synchronization.ResourceType.ACCOUNT;
import static be.looorent.ponto.synchronization.SynchronizationSubtype.ACCOUNT_DETAILS;
import static be.looorent.ponto.synchronization.SynchronizationSubtype.ACCOUNT_TRANSACTIONS;

@Slf4j
class SynchronizationHttpClient implements SynchronizationClient {

    private static final String PATH = "/synchronizations";
    private static final String INVALID_SYNCHRONIZATION_RESOURCE_CODE = "invalidSynchronizationResource";

    private final HttpClient http;

    SynchronizationHttpClient(HttpClient http) {
        this.http = http;
    }

    @Override
    public Optional<Synchronization> find(@NonNull UUID synchronizationId) {
        return http.get(PATH, synchronizationId, SynchronizationMapping.Data.class);
    }

    @Override
    public Synchronization synchronizeAccountTransactions(@NonNull UUID accountId) throws ResourceNotFoundException {
        return synchronize(accountId, ACCOUNT, ACCOUNT_TRANSACTIONS);
    }

    @Override
    public Synchronization synchronizeAccountDetails(@NonNull UUID accountId) throws ResourceNotFoundException {
        return synchronize(accountId, ACCOUNT, ACCOUNT_DETAILS);
    }

    private Synchronization synchronize(UUID resourceId,
                                        ResourceType type,
                                        SynchronizationSubtype subtype) throws ResourceNotFoundException {
        var body = new JsonApi.Single<>(NewSynchronization.build(resourceId, type, subtype));
        try {
            return this.http.post(PATH, body, SynchronizationMapping.Data.class);
        } catch(HttpClientException e) {
            var resourceNotFound = e.getErrors()
                    .stream()
                    .map(HttpErrors.HttpError::getCode)
                    .anyMatch(error -> error.equalsIgnoreCase(INVALID_SYNCHRONIZATION_RESOURCE_CODE));
            if (resourceNotFound) {
                throw new ResourceNotFoundException(e, resourceId);
            } else {
                throw e;
            }
        }
    }
}
