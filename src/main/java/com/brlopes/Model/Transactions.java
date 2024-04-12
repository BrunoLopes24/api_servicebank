package com.brlopes.Model;

import java.sql.Date;

import com.brlopes.Model.enums.TransactionEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
public class Transactions {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O valor da chave primária será gerado automaticamente pelo banco de dados durante a inserção da linha na tabela
    private Long transaction_id;
    
    private Double value;
    private Date date;
    private Double tax;
    private Double totalAmmount;
    
    @Enumerated
    private TransactionEnum state;
    
    @ManyToOne
    private Client client;
    @ManyToOne
    private Client destinyClient;
    
    public Transactions() {
    }
    
    public Transactions(Client destinyClient, Double value, Date date, Double tax, Double totalAmmount, Client client, TransactionEnum state) {
        this.destinyClient = destinyClient;
        this.value = value;
        this.date = date;
        this.tax = tax;
        this.totalAmmount = totalAmmount;
        this.client = client;
        this.state = state;
    }
}
