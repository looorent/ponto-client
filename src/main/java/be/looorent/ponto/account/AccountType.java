package be.looorent.ponto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccountType {
    @JsonProperty("checking") CHECKING,
    @JsonProperty("savings") SAVINGS
}
