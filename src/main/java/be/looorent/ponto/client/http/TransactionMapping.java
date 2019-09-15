package be.looorent.ponto.client.http;

import be.looorent.ponto.transaction.RemittanceInformationType;
import be.looorent.ponto.transaction.Transaction;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

class TransactionMapping {
    @Value
    static class Data implements JsonMapping<Transaction> {
        private UUID id;
        private Attributes attributes;
        private Relationships relationships;

        @Override
        public Transaction toEntity() {
            return new Transaction(id,
                    attributes.getValueDate(),
                    attributes.getRemittanceInformationType(),
                    attributes.getRemittanceInformation(),
                    attributes.getExecutionDate(),
                    attributes.getDescription(),
                    attributes.getCurrency(),
                    attributes.getCounterpartReference(),
                    attributes.getCounterpartName(),
                    attributes.getAmount(),
                    relationships.getAccount().getId()
            );
        }
    }

    @Value
    static class Attributes {
        private LocalDateTime valueDate;
        private RemittanceInformationType remittanceInformationType;
        private String remittanceInformation;
        private LocalDateTime executionDate;
        private String description;
        private Currency currency;
        private String counterpartReference;
        private String counterpartName;
        private BigDecimal amount;
    }

    @Value
    static class Relationships {
        private JsonApi.SingleRelationship account;
    }
}
