package com.brlopes.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brlopes.Model.Client;
import com.brlopes.Model.Login;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.exceptions.AuthenticationException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class LoginService {
    
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private LoginRepo loginRepo;
    
    public ResponseEntity<String> authenticateUser(String username, String password) {
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
            
            saveLogin(username, token, expiresAt.toString());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Se a autenticação falhar devido a senha inválida, retornar uma mensagem de erro com status 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (ResourceNotFoundException e) {
            // Se o username não for encontrado na base de dados, retornar uma mensagem de erro com status 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
    }
    
    public void saveLogin(String username, String jwt_token, String jwt_time) {
        // Crie uma nova instância de Login com os dados fornecidos
        Login login = new Login();
        login.setUsername(username);
        login.setJwt_token(jwt_token);
        login.setJwt_timeLimit(jwt_time);
        
        // Salve a instância no banco de dados
        loginRepo.save(login);
    }
}