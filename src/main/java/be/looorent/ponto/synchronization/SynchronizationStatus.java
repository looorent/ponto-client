package be.looorent.ponto.synchronization;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SynchronizationStatus {
    @JsonProperty("pending")
    PENDING(false),

    @JsonProperty("running")
    RUNNING(false),

    @JsonProperty("success")
    SUCCESS(true),

    @JsonProperty("error")
    ERROR(true);

    private final boolean complete;

    SynchronizationStatus(boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }
}
