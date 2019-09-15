package be.looorent.ponto.institution;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;

import java.util.Optional;
import java.util.UUID;

public interface FinancialInstitutionClient {
    Optional<FinancialInstitution> find(UUID id);
    CollectionResponse<FinancialInstitution> findAll();
    CollectionResponse<FinancialInstitution> findAll(Page page);
}
