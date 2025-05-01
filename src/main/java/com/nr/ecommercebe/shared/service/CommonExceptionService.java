package com.nr.ecommercebe.shared.service;

import com.nr.ecommercebe.shared.exception.RecordExists;

public class CommonExceptionService {
    private CommonExceptionService() {
        // Prevent instantiation
    }
    public static void throwRecordExists(Boolean isExists, String message) {
        if (Boolean.TRUE.equals(isExists)) {
            throw new RecordExists(message);
        }
    }
}
