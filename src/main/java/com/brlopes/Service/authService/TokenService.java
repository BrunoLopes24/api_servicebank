package com.brlopes.Service.authService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.brlopes.Model.Login;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;
    
    public String generateToken(Login login){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
            .withIssuer("auth-api")
            .withSubject(login.getUsername())
            .withExpiresAt(generateExpirationDate())
            .sign(algorithm);
            
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }
    
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
            .withIssuer("auth-api")
            .build()
            .verify(token)
            .getSubject();
            
        } catch (JWTVerificationException e) {
            return "";
        }
    }
    
    /* private Instant generateExpirationDate(){
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.UTC); // Token works for 5 minutes.
    } */
    
    private Instant generateExpirationDate(){
        ZoneId lisbonZone = ZoneId.of("Europe/Lisbon");
        LocalDateTime expirationTime = LocalDateTime.now(lisbonZone).plusMinutes(5);
        return expirationTime.atZone(lisbonZone).toInstant();
    }
}
