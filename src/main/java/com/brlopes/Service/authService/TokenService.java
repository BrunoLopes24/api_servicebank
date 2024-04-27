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

/**
 * The TokenService class provides methods to generate and validate JWT tokens.
 * It uses the JWT library for token generation and validation.
 * It uses the @Service annotation to indicate that it is a Spring Boot service.
 */
@Service
public class TokenService {

    /**
     * The secret key for token generation and validation.
     * It is injected from the application properties file.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * This method generates a JWT token for a given login.
     * It uses the HMAC256 algorithm for token generation.
     * The token is set to expire in 5 minutes.
     *
     * @param login the login for which the token is generated.
     * @return the generated token as a String.
     * @throws RuntimeException if an error occurs during token generation.
     */
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

    /**
     * This method validates a given JWT token.
     * It uses the HMAC256 algorithm for token validation.
     * It returns the subject of the token if the token is valid.
     *
     * @param token the token to be validated.
     * @return the subject of the token as a String if the token is valid, or an empty string if the token is invalid.
     */
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

    /**
     * This method generates the expiration date for the token.
     * The token is set to expire in 5 minutes from the current time.
     * The time is calculated based on the "Europe/Lisbon" timezone.
     *
     * @return the expiration date as an Instant.
     */
    private Instant generateExpirationDate(){
        ZoneId lisbonZone = ZoneId.of("Europe/Lisbon");
        LocalDateTime expirationTime = LocalDateTime.now(lisbonZone).plusMinutes(5);
        return expirationTime.atZone(lisbonZone).toInstant();
    }
}