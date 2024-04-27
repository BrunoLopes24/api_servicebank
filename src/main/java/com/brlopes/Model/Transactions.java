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

/**
 * The Transactions class is an entity class in Spring Boot.
 * It represents a transaction in the system.
 * This class uses Lombok annotations for automatic generation of getters, setters, and other common methods.
 * It uses the @Entity annotation to indicate that it is a JPA entity.
 */
@Getter
@Setter
@Data
@Entity
@Table(name = "Transactions")
public class Transactions {

    /**
     * The ID of the transaction. It is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_id;

    /**
     * The value of the transaction.
     */
    private Double value;

    /**
     * The date of the transaction.
     */
    private Date date;

    /**
     * The tax of the transaction.
     */
    private Double tax;

    /**
     * The total amount of the transaction.
     */
    private Double totalAmmount;

    /**
     * The state of the transaction. It is an enum of TransactionEnum.
     */
    @Enumerated
    private TransactionEnum state;

    /**
     * The client who sent the transaction.
     * It is a many-to-one relationship with the Client entity.
     */
    @ManyToOne
    @JoinColumn(name = "sender_client_id")
    private Client client;

    /**
     * The client who is the recipient of the transaction.
     * It is a many-to-one relationship with the Client entity.
     */
    @ManyToOne
    @JoinColumn(name = "destiny_client_id")
    private Client destinyClient;

    /**
     * Default constructor.
     * It is needed for the serialization and deserialization of objects.
     */
    public Transactions() {
    }

    /**
     * Constructor with value, date, tax, and TransactionEnum parameters.
     * It is used to create a new instance of Transactions with provided value, date, tax, and state.
     * The total amount is calculated by the TotalAmmount method.
     *
     * @param value The value of the transaction.
     * @param date The date of the transaction.
     * @param tax The tax of the transaction.
     * @param state The state of the transaction.
     */
    public Transactions(Double value, Date date, Double tax, TransactionEnum state) {
        this.value = value;
        this.date = date;
        this.tax = tax;
        this.totalAmmount = TotalAmmount();
        this.state = state;
    }

    /**
     * This method calculates the total amount of the transaction.
     * It adds the value and the tax.
     *
     * @return the total amount as a Double.
     */
    private Double TotalAmmount(){
        return tax + value;
    }
}