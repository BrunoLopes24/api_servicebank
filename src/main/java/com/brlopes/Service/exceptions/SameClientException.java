package com.brlopes.Service.exceptions;

public class SameClientException extends RuntimeException  {
    public SameClientException(String message) {
        super(message);
    }
}
