package com.digiconnect.pro.authentication.exceptions;

public class AlreadyConnectedException extends RuntimeException {
    public AlreadyConnectedException(String message) {
        super(message);
    }
}
