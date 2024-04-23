package com.brlopes.dto;

import com.brlopes.Model.Transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private Transactions transaction;
    private Double totalAmount;
    private String clientName;
    private String destinyClientName;
    private String transactionDate;

    public TransactionDTO() {
    }
    
    public TransactionDTO(Double totalAmount, String clientName, String destinyClientName, String transactionDate) {
        this.totalAmount = totalAmount;
        this.clientName = clientName;
        this.destinyClientName = destinyClientName;
        this.transactionDate = transactionDate;
    }
}

