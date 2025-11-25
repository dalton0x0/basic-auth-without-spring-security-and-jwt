package com.digiconnect.pro.authentication.exceptions;

public class NoSuchUserException extends RuntimeException{
    public NoSuchUserException(String message) {
        super(message);
    }
}
