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

@Getter
@Setter
@Data
@Entity
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;
    
    private String name;
    private String username;
    private String email;
    private String password;
    private Integer age;
    private LoginRoles role;
    private Double balance = 0.0;
    
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> sentTransactions;
    
    @JsonIgnore
    @OneToMany(mappedBy = "destinyClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> receiveTransactions;
    
    public Client() {
    }
    
    public Client(String name, String username, String email, String password, Integer age) {
        this.name = name;
        this.username = username;
        this.email = email;
        setPassword(password);
        this.age = age;
        this.role = LoginRoles.CLIENT;
        this.balance = 0.0;
    }
    
    public void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
