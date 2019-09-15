package be.looorent.ponto.synchronization;

import java.util.Optional;
import java.util.UUID;

public interface SynchronizationClient {
    Optional<Synchronization> find(UUID synchronizationId);
    Synchronization synchronizeAccountTransactions(UUID accountId) throws ResourceNotFoundException;
    Synchronization synchronizeAccountDetails(UUID accountId) throws ResourceNotFoundException;
}
