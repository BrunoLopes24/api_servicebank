package com.brlopes.Model;

import java.sql.Date;

import com.brlopes.Model.enums.TransactionEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "Transactions")
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
    @JoinColumn(name = "sender_client_id") // Nome da coluna na tabela de Transactions que armazena o ID do cliente que enviou a transação
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "destiny_client_id") // Nome da coluna na tabela de Transactions que armazena o ID do cliente destinatário da transação
    private Client destinyClient;
    
    public Transactions() {
    }
    
    public Transactions(Double value, Date date, Double tax, TransactionEnum state) {
        this.value = value;
        this.date = date;
        this.tax = tax;
        this.totalAmmount = TotalAmmount();
        this.state = state;
    }
    
    private Double TotalAmmount(){
        return tax + value;
    }
}
