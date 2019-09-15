package be.looorent.ponto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RemittanceInformationType {
    @JsonProperty("structured") STRUCTURED,
    @JsonProperty("unstructured") UNSTRUCTURED
}
