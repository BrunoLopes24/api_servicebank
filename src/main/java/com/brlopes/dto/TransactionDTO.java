package com.brlopes.dto;

import java.io.Serializable;
import java.sql.Date;

import com.brlopes.Model.Client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO implements Serializable {
    
    private Date date;
    private Double totalAmount;
    private String clientName;
    private Integer clientAge;

    
    public TransactionDTO() {
    }
    
    public TransactionDTO(Date date, Double totalAmount, String clientName, Integer clientAge) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.clientName = clientName;
        this.clientAge = clientAge;
    }

    public TransactionDTO(Date date, Double totalAmount, Client client) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.clientName = client.getName(); // Supondo que há um método getName() em Client
        this.clientAge = client.getAge(); // Supondo que há um método getAge() em Client
    }
}
