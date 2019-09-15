package be.looorent.ponto.client.http;

import be.looorent.ponto.institution.FinancialInstitution;
import lombok.Value;

import java.util.UUID;

class FinancialInstitutionMapping {

    @Value
    static class Data implements JsonMapping {
        private UUID id;
        private Attributes attributes;

        @Override
        public FinancialInstitution toEntity() {
            return new FinancialInstitution(
                    id,
                    attributes.getName()
            );
        }
    }

    @Value
    static class Attributes {
        private String name;
    }
}
