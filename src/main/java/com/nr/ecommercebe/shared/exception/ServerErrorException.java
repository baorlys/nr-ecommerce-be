package com.nr.ecommercebe.shared.exception;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
