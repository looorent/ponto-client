package be.looorent.ponto.client.http;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;
import be.looorent.ponto.synchronization.Synchronization;
import lombok.Value;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

public class JsonApi {
    @Value
    static class Single<T> {
        private T data;
    }

    @Value
    static class Collection<T> implements CollectionResponse<T> {
        private Meta meta;
        private Links links;
        private java.util.Collection<T> data;

        @Override
        public Page getPage() {
            return meta.getPaging();
        }

        @Override
        public java.util.Collection<T> getEntities() {
            return data;
        }

        @Override
        public Optional<LocalDateTime> getSynchronizedAt() {
            return ofNullable(meta)
                    .map(Meta::getSynchronizedAt);
        }

        @Override
        public Optional<Synchronization> getLatestSynchronization() {
            return ofNullable(meta)
                    .map(Meta::getLatestSynchronization)
                    .map(JsonMapping::toEntity);
        }
    }

    @Value
    static class Links {
        private URL first;
        private URL prev;
        private URL next;
    }

    @Value
    static class Meta {
        private Page paging;
        private SynchronizationMapping.Data latestSynchronization;
        private LocalDateTime synchronizedAt;
    }

    @Value
    static class RelationshipLinks {
        private URL related;
    }

    @Value
    static class CollectionRelationship {
        private RelationshipLinks links;
    }

    @Value
    static class SingleRelationship {
        private Data data;
        private RelationshipLinks links;

        @Value
        static class Data {
            private String type;
            private UUID id;
        }

        public UUID getId() {
            return data.getId();
        }
    }
}
