package com.brlopes.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brlopes.Model.Client;
import com.brlopes.Model.Login;
import com.brlopes.Model.enums.LoginRoles;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.exceptions.AuthenticationException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * The LoginService class provides methods for user authentication and login management.
 * It implements the UserDetailsService interface for loading user-specific data.
 * It uses the @Service annotation to indicate that it is a Spring Boot service.
 * It uses the ClientRepo and LoginRepo repositories for database operations.
 */
@Service
public class LoginService implements UserDetailsService {

    /**
     * The ClientRepo repository used for client database operations.
     */
    @Autowired
    private ClientRepo clientRepo;

    /**
     * The LoginRepo repository used for login database operations.
     */
    @Autowired
    private LoginRepo loginRepo;

    /**
     * This method authenticates a user by username and password.
     * It generates a JWT token for the authenticated user.
     * It saves the login information in the database.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param role the role of the user.
     * @return a ResponseEntity with the JWT token if authentication is successful, or an error message if authentication fails.
     */
    public ResponseEntity<String> authenticateUser(String username, String password, LoginRoles role) {
        try {
            // Buscar o cliente pelo username
            Client client = clientRepo.findByUsername(username);
            if (client == null) {
                throw new ResourceNotFoundException("Client not found with username: " + username);
            }
            
            // Verificar a senha usando BCrypt
            if (!BCrypt.verifyer().verify(password.toCharArray(), client.getPassword()).verified) {
                throw new AuthenticationException("Invalid password");
            }
            
            // Definir a data de expiração para 5 minutos a partir do momento atual
            Date expiresAt = new Date(System.currentTimeMillis() + (5 * 60 * 1000));
            
            // Gerar token JWT com tempo de expiração
            Algorithm algorithm = Algorithm.HMAC256("servicebank");
            String token = JWT.create()
            .withSubject(username)
            .withIssuer("lopes")
            .withExpiresAt(expiresAt) // Defina a expiração do token
            .sign(algorithm);
            
            saveLogin(username, role);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Se a autenticação falhar devido a senha inválida, retornar uma mensagem de erro com status 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (ResourceNotFoundException e) {
            // Se o username não for encontrado na base de dados, retornar uma mensagem de erro com status 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
    }

    /**
     * This method saves a login in the database.
     *
     * @param username the username of the login.
     * @param role the role of the login.
     */
    public void saveLogin(String username, LoginRoles role) {
        // Crie uma nova instância de Login com os dados fornecidos
        Login login = new Login();
        login.setUsername(username);
        login.setRole(role);
        
        // Salve a instância no banco de dados
        loginRepo.save(login);
    }

    /**
     * This method loads user-specific data by username.
     * It is required by the UserDetailsService interface.
     *
     * @param username the username of the user.
     * @return a UserDetails object with the user-specific data.
     * @throws UsernameNotFoundException if the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepo.findByUsername(username);
    }
}