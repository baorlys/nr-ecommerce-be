package com.nr.ecommercebe.common.exception;

public class RecordExists extends RuntimeException {
    public RecordExists(String message) {
        super(message);
    }
}
