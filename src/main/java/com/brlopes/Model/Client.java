package com.brlopes.Model;

import java.util.List;

import com.brlopes.Model.enums.LoginRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * The Client class is an entity class in Spring Boot.
 * It represents a client in the system.
 * This class uses Lombok annotations for automatic generation of getters, setters, and other common methods.
 * It uses the @Entity annotation to indicate that it is a JPA entity.
 */
@Getter
@Setter
@Data
@Entity
public class Client {

    /**
     * The ID of the client. It is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

    /**
     * The name of the client.
     */
    private String name;

    /**
     * The username of the client.
     */
    private String username;

    /**
     * The email of the client.
     */
    private String email;

    /**
     * The password of the client. It is hashed using BCrypt.
     */
    private String password;

    /**
     * The age of the client.
     */
    private Integer age;

    /**
     * The role of the client. It is an enum of LoginRoles.
     */
    private LoginRoles role;

    /**
     * The balance of the client. It is initialized to 0.0.
     */
    private Double balance = 0.0;

    /**
     * The transactions sent by the client.
     * It is a one-to-many relationship with the Transactions entity.
     * It is ignored in JSON serialization.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> sentTransactions;

    /**
     * The transactions received by the client.
     * It is a one-to-many relationship with the Transactions entity.
     * It is ignored in JSON serialization.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "destinyClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> receiveTransactions;

    /**
     * Default constructor.
     * It is needed for the serialization and deserialization of objects.
     */
    public Client() {
    }

    /**
     * Constructor with all properties.
     * It is used to create a new instance of Client with provided name, username, email, password, and age.
     * The role is set to CLIENT and the balance is set to 0.0.
     *
     * @param name The name of the client.
     * @param username The username of the client.
     * @param email The email of the client.
     * @param password The password of the client.
     * @param age The age of the client.
     */
    public Client(String name, String username, String email, String password, Integer age) {
        this.name = name;
        this.username = username;
        this.email = email;
        setPassword(password);
        this.age = age;
        this.role = LoginRoles.CLIENT;
        this.balance = 0.0;
    }

    /**
     * This method sets the password of the client.
     * It hashes the password using BCrypt.
     *
     * @param password The password of the client.
     */
    public void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}