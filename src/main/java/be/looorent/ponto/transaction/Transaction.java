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
     * @return Date representing the moment the transaction is considered effective
     */
    private LocalDateTime valueDate;

    /**
     * @return Type of remittance information
     */
    private RemittanceInformationType remittanceInformationType;

    /**
     * @return Content of the remittance information (aka communication)
     */
    private String remittanceInformation;

    /**
     * @return Date representing the moment the transaction has been recorded
     */
    private LocalDateTime executionDate;

    /**
     * @return Description of the transaction
     */
    private String description;

    /**
     * @return Currency of the transaction
     */
    private Currency currency;

    /**
     * @return Number representing the counterpart's account
     */
    private String counterpartReference;

    /**
     * @return Legal name of the counterpart
     */
    private String counterpartName;

    /**
     * @return Amount of the transaction. Can be positive or negative
     */
    private BigDecimal amount;

    /**
     * @return Id of the account that this transaction belongs to
     */
    private UUID accountId;
}
