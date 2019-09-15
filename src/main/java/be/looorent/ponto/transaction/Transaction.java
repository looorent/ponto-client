package be.looorent.ponto.transaction;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Date representing the moment the transaction is considered effective
     */
    private LocalDateTime valueDate;

    /**
     * Type of remittance information
     */
    private RemittanceInformationType remittanceInformationType;

    /**
     * Content of the remittance information (aka communication)
     */
    private String remittanceInformation;

    /**
     * Date representing the moment the transaction has been recorded
     */
    private LocalDateTime executionDate;

    /**
     * Description of the transaction
     */
    private String description;

    /**
     * Currency of the transaction
     */
    private Currency currency;

    /**
     * Number representing the counterpart's account
     */
    private String counterpartReference;

    /**
     * Legal name of the counterpart
     */
    private String counterpartName;

    /**
     * Amount of the transaction. Can be positive or negative
     */
    private BigDecimal amount;

    /**
     * Id of the account that this transaction belongs to
     */
    private UUID accountId;
}
