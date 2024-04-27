package com.brlopes.dto;

import com.brlopes.Model.Transactions;

import lombok.Getter;
import lombok.Setter;

/**
 * The TransactionDTO class is a Data Transfer Object (DTO) class.
 * It is used to transfer data between processes.
 * As a DTO, it should only contain fields for data transfer and should not contain any business logic.
 * This class uses Lombok annotations for automatic generation of getters, setters, and other common methods.
 */
@Getter
@Setter
public class TransactionDTO {

    /**
     * The transaction details.
     */
    private Transactions transaction;

    /**
     * The total amount of the transaction.
     */
    private Double totalAmount;

    /**
     * The name of the client involved in the transaction.
     */
    private String clientName;

    /**
     * The name of the destiny client involved in the transaction.
     */
    private String destinyClientName;

    /**
     * The date of the transaction.
     */
    private String transactionDate;

    /**
     * Default constructor.
     * It is needed for the serialization and deserialization of objects.
     */
    public TransactionDTO() {
    }

    /**
     * Constructor with all properties.
     * It is used to create a new instance of TransactionDTO with provided total amount, client name, destiny client name, and transaction date.
     *
     * @param totalAmount The total amount of the transaction.
     * @param clientName The name of the client involved in the transaction.
     * @param destinyClientName The name of the destiny client involved in the transaction.
     * @param transactionDate The date of the transaction.
     */
    public TransactionDTO(Double totalAmount, String clientName, String destinyClientName, String transactionDate) {
        this.totalAmount = totalAmount;
        this.clientName = clientName;
        this.destinyClientName = destinyClientName;
        this.transactionDate = transactionDate;
    }
}