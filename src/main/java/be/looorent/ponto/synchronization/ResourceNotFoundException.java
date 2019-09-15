package be.looorent.ponto.synchronization;

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

public class ResourceNotFoundException extends Exception {

    @Getter
    private final UUID resourceId;

    public ResourceNotFoundException(Throwable cause, @NonNull UUID resourceId) {
        super(cause);
        this.resourceId = resourceId;
    }
}
