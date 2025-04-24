package com.nr.ecommercebe.common.service;

import com.nr.ecommercebe.common.exception.RecordExists;

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
