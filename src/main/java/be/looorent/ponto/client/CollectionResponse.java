package be.looorent.ponto.client;

import be.looorent.ponto.synchronization.Synchronization;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * More than a simple collection of entities, this also stores
 * the response page.
 * @param <T> the type of entities
 */
public interface CollectionResponse<T> {
    Page getPage();
    Collection<T> getEntities();

    Optional<LocalDateTime> getSynchronizedAt();
    Optional<Synchronization> getLatestSynchronization();

    default boolean hasBefore() {
        return getPage().hasBefore();
    }

    default boolean hasAfter() {
        return getPage().hasAfter();
    }
}
