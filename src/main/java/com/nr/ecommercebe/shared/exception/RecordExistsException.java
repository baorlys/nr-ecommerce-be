package com.nr.ecommercebe.shared.exception;

public class RecordExistsException extends RuntimeException {
    public RecordExistsException(String message) {
        super(message);
    }
}
