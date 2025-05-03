package com.nr.ecommercebe.shared.exception;

public class RecordExists extends RuntimeException {
    public RecordExists(String message) {
        super(message);
    }
}
