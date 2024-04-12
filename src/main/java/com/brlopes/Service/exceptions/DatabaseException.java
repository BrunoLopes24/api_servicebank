package com.brlopes.Service.exceptions;

public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String msg){
        super(msg);
    }
}
