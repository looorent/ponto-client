package be.looorent.ponto.client;

import be.looorent.ponto.account.AccountClient;
import be.looorent.ponto.institution.FinancialInstitutionClient;
import be.looorent.ponto.synchronization.SynchronizationClient;
import be.looorent.ponto.transaction.TransactionClient;

public interface PontoClient {
    FinancialInstitutionClient financialInstitutions();
    SynchronizationClient synchronizations();
    AccountClient accounts();
    TransactionClient transactions();
}
