package com.brlopes.Service.exceptions;

public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String msg){
        super(msg);
    }
}
