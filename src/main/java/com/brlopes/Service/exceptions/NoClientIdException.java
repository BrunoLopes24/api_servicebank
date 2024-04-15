package com.brlopes.Service.exceptions;

public class NoClientIdException extends RuntimeException  {
    public NoClientIdException(String message) {
        super(message);
    }
}
