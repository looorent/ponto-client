package be.looorent.ponto.synchronization;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

/**
 * This is an object representing a resource synchronization.
 * This object will give you the details of the synchronization, including its resource, type, and status.
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Synchronization {
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Current status of the synchronization, which changes from pending to running to success or error
     */
    private SynchronizationStatus status;

    /**
     * What is being synchronized. Account information such as balance is updated using accountDetails, while accountTransactions is used to synchronize the transactions.
     */
    private SynchronizationSubtype subtype;

    /**
     * When this synchronization was created.
     */
    private Instant createdAt;

    /**
     * When this synchronization was last synchronized successfully.
     */
    private Instant updatedAt;

    /**
     * Identifier of the resource to be synchronized
     */
    private UUID resourceId;

    /**
     * Type of the resource to be synchronized.
     */
    private ResourceType resourceType;

    // TODO use strong typing
    /**
     * Details of any errors that have occurred during synchronization, due to invalid authorization or technical failure
     */
    private Collection<Object> errors;

    public boolean isComplete() {
        return status.isComplete();
    }
}
