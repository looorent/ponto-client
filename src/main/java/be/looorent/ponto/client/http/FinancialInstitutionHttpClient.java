package be.looorent.ponto.client.http;

import be.looorent.ponto.client.CollectionResponse;
import be.looorent.ponto.client.Page;
import be.looorent.ponto.institution.FinancialInstitution;
import be.looorent.ponto.institution.FinancialInstitutionClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static be.looorent.ponto.client.Page.DEFAULT_PAGE;

@Slf4j
class FinancialInstitutionHttpClient implements FinancialInstitutionClient {

    private static final String PATH = "/financial-institutions";

    private final HttpClient http;

    FinancialInstitutionHttpClient(@NonNull HttpClient http) {
        this.http = http;
    }

    @Override
    public Optional<FinancialInstitution> find(@NonNull UUID id) {
        return http.get(
                PATH,
                id,
                FinancialInstitutionMapping.Data.class
            );
    }

    @Override
    public CollectionResponse<FinancialInstitution> findAll() {
        return findAll(DEFAULT_PAGE);
    }

    @Override
    public CollectionResponse<FinancialInstitution> findAll(Page page) {
        if (page == null) {
            return findAll();
        } else {
            return http.list(
                    PATH,
                    page,
                    FinancialInstitutionMapping.Data.class
            );
        }
    }
}
