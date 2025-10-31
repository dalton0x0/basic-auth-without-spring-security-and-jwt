package com.digiconnect.pro.authentication.exceptions;

public class RoleNameAlreadyInUseException extends RuntimeException {
    public RoleNameAlreadyInUseException(String message) {
        super(message);
    }
}
