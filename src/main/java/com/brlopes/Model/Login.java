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

/**
 * The Login class is an entity class in Spring Boot.
 * It represents a login in the system.
 * This class implements UserDetails interface which provides core user information.
 * It uses the @Entity annotation to indicate that it is a JPA entity.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Login implements UserDetails {

    /**
     * The ID of the login. It is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username of the login.
     */
    private String username;

    /**
     * The password of the login.
     */
    private String password;

    /**
     * The role of the login. It is an enum of LoginRoles.
     */
    private LoginRoles role;

    /**
     * Constructor with Client and LoginRoles parameters.
     * It is used to create a new instance of Login with provided client and role.
     *
     * @param client The client object.
     * @param role The role of the login.
     */
    public Login(Client client, LoginRoles role) {
        this.username = client.getUsername();
        this.role = role;
    }

    /**
     * Constructor with username, password, and LoginRoles parameters.
     * It is used to create a new instance of Login with provided username, password, and role.
     *
     * @param username The username of the login.
     * @param password The password of the login.
     * @param role The role of the login.
     */
    public Login(String username, String password, LoginRoles role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * This method returns the authorities granted to the user.
     * It returns a collection of GrantedAuthority, which represents an authority granted to an Authentication object.
     *
     * @return a collection of GrantedAuthority.
     */
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

    /**
     * This method returns the username of the login.
     *
     * @return the username as a String.
     */
    @Override
    public String getUsername(){
        return username;
    }

    /**
     * This method checks if the account is not expired.
     *
     * @return true as the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * This method checks if the account is not locked.
     *
     * @return true as the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * This method checks if the credentials are not expired.
     *
     * @return true as the credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * This method checks if the account is enabled.
     *
     * @return true as the account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}