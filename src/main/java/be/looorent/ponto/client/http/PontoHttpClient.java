package be.looorent.ponto.client.http;

import be.looorent.ponto.account.AccountClient;
import be.looorent.ponto.client.Configuration;
import be.looorent.ponto.client.PontoClient;
import be.looorent.ponto.institution.FinancialInstitutionClient;
import be.looorent.ponto.synchronization.SynchronizationClient;
import be.looorent.ponto.transaction.TransactionClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PontoHttpClient implements PontoClient {

    private final Configuration configuration;
    private final ObjectMapper mapper;

    PontoHttpClient(Configuration configuration) {
        this.configuration = configuration;
        this.mapper = JsonMapper.create();
    }

    public static PontoClient from(Configuration configuration) {
        return new PontoHttpClient(configuration);
    }

    @Override
    public FinancialInstitutionClient financialInstitutions() {
        return new FinancialInstitutionHttpClient(new HttpClient(configuration, mapper));
    }

    @Override
    public SynchronizationClient synchronizations() {
        return new SynchronizationHttpClient(new HttpClient(configuration, mapper));
    }

    @Override
    public AccountClient accounts() {
        return new AccountHttpClient(new HttpClient(configuration, mapper));
    }

    @Override
    public TransactionClient transactions() {
        return new TransactionHttpClient(new HttpClient(configuration, mapper));
    }

}
