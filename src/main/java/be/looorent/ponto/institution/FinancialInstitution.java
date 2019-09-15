package be.looorent.ponto.institution;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FinancialInstitution {
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * @return Name of the financial institution
     */
    private String name;
}
