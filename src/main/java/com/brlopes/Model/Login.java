package com.brlopes.Model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.brlopes.Model.enums.LoginRoles;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Login implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String username;
    private String password;
    private LoginRoles role;
    
    public Login(Client client, LoginRoles role) {
        this.username = client.getUsername();
        this.role = role;
    }
    
    public Login(String username, String password, LoginRoles role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == LoginRoles.ADMIN){
            return List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN"), 
            new SimpleGrantedAuthority("ROLE_CLIENT"));
        }else{
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
    }
    
    @Override
    public String getUsername(){
        return username; 
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
