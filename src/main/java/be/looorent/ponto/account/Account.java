package be.looorent.ponto.account;

import be.looorent.ponto.synchronization.Synchronization;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

/**
 * This object will provide details about the account, including the balance and the currency.
 * An account has related transactions and belongs to a financial institution.
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * @return Type of account
     */
    private AccountType type;

    /**
     * @return Type of financial institution reference (such as IBAN)
     */
    private String referenceType;

    /**
     * @return Financial institution's internal reference for this account
     */
    private String reference;

    /**
     * @return Description of the account
     */
    private String description;

    /**
     * @return Total funds currently in the account
     */
    private BigDecimal currentBalance;

    /**
     * @return Amount of account funds that can be accessed immediately
     */
    private BigDecimal availableBalance;

    /**
     * @return Currency of the account
     */
    private Currency currency;

    /**
     * @return Id of the financial institution that hosts this account
     */
    private UUID financialInstitutionId;

    /**
     * @return When this account was last synchronized.
     */
    private LocalDateTime synchronizedAt;

    /**
     * @return Details of the most recently completed (with success or error) synchronization of the account
     */
    private Synchronization latestSynchronization;
}