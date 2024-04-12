package com.brlopes.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Login {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String jwt_token;
    private String jwt_timeLimit;
    
    public Login() {
    }
    
    public Login(Client client, String jwt_token, String jwt_time) {
        this.username = client.getUsername();
        this.jwt_token = jwt_token;
        this.jwt_timeLimit = jwt_time;
    }
}
