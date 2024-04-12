package com.brlopes.dto;

import java.io.Serializable;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO implements Serializable {

    private Date date;
    private Double totalAmount;
    private String clientName;
    private Integer clientAge;

    public TransactionDTO(Date date, Double totalAmount, String clientName, Integer clientAge) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.clientName = clientName;
        this.clientAge = clientAge;
    }
}
