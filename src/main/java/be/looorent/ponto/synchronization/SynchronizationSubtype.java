package be.looorent.ponto.synchronization;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SynchronizationSubtype {
    @JsonProperty("accountDetails") ACCOUNT_DETAILS,
    @JsonProperty("accountTransactions") ACCOUNT_TRANSACTIONS
}
