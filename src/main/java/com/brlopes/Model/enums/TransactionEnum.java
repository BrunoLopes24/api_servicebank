package com.brlopes.Model.enums;

/**
 * The TransactionEnum is an enumeration that represents the different states a transaction can be in.
 * It has four states: IN_PROCESS, PROCESSED, CANCELLED, and ERROR.
 * Each state is represented as an enum constant.
 * This enum is typically used to track the status of a transaction.
 */
public enum TransactionEnum {
    /**
     * The IN_PROCESS state. It represents a transaction that is currently being processed.
     */
    IN_PROCESS,

    /**
     * The PROCESSED state. It represents a transaction that has been successfully processed.
     */
    PROCESSED,

    /**
     * The CANCELLED state. It represents a transaction that has been cancelled.
     */
    CANCELLED,

    /**
     * The ERROR state. It represents a transaction that has encountered an error during processing.
     */
    ERROR
}