package com.brlopes.Service.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    
    public DataIntegrityViolationException(String msg){
        super(msg);
    }
}
