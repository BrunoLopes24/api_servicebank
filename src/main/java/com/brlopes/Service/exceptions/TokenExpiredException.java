package com.brlopes.Service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String msg) {
        super(msg);
    }

    public ResponseEntity<String> responseEntity() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("Token JWT expirado. Faça o Registo/Login novamente para adicionar transacção");
    }
}