package be.looorent.ponto.client.http;

import be.looorent.ponto.account.Account;
import be.looorent.ponto.account.AccountType;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

class AccountMapping {

    @Value
    static class Data implements JsonMapping<Account> {
        private UUID id;
        private Attributes attributes;
        private Meta meta;
        private Relationships relationships;

        @Override
        public Account toEntity() {
            return new Account(id,
                    attributes.getType(),
                    attributes.getReferenceType(),
                    attributes.getReference(),
                    attributes.getDescription(),
                    attributes.getCurrentBalance(),
                    attributes.getAvailableBalance(),
                    attributes.getCurrency(),
                    relationships.getFinancialInstitution().getId(),
                    meta.getSynchronizedAt(),
                    meta.getLatestSynchronization().toEntity()
            );
        }
    }

    @Value
    static class Meta {
        private LocalDateTime synchronizedAt;
        private SynchronizationMapping.Data latestSynchronization;
    }

    @Value
    static class Attributes {
        private AccountType type;
        private String referenceType;
        private String reference;
        private String description;
        private BigDecimal currentBalance;
        private BigDecimal availableBalance;
        private Currency currency;
    }

    @Value
    static class Relationships {
        private JsonApi.CollectionRelationship transactions;
        private JsonApi.SingleRelationship financialInstitution;
    }
}
