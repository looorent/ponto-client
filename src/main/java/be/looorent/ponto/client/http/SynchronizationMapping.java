package be.looorent.ponto.client.http;

import be.looorent.ponto.synchronization.ResourceType;
import be.looorent.ponto.synchronization.Synchronization;
import be.looorent.ponto.synchronization.SynchronizationStatus;
import be.looorent.ponto.synchronization.SynchronizationSubtype;
import lombok.Value;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

class SynchronizationMapping {
    @Value
    static class Data implements JsonMapping<Synchronization> {
        private UUID id;
        private Attributes attributes;

        @Override
        public Synchronization toEntity() {
            return new Synchronization(
                    id,
                    attributes.getStatus(),
                    attributes.getSubtype(),
                    attributes.getCreatedAt(),
                    attributes.getUpdatedAt(),
                    attributes.getResourceId(),
                    attributes.getResourceType(),
                    attributes.getErrors()
            );
        }
    }

    @Value
    static class Attributes {
        private SynchronizationStatus status;
        private SynchronizationSubtype subtype;
        private Instant createdAt;
        private Instant updatedAt;
        private UUID resourceId;
        private ResourceType resourceType;
        private Collection<Object> errors;
    }

    @Value
    static class NewSynchronization {
        private String type;
        private Attributes attributes;

        @Value
        static class Attributes {
            private UUID resourceId;
            private ResourceType resourceType;
            private SynchronizationSubtype subtype;
        }

        static NewSynchronization build(UUID resourceId,
                                        ResourceType type,
                                        SynchronizationSubtype subtype) {
            return new NewSynchronization("synchronization",
                    new Attributes(resourceId, type, subtype));
        }
    }
}
