package com.nr.ecommercebe.shared.exception;

import java.time.LocalDateTime;


public record ErrorResponse(LocalDateTime timestamp, int status, String message, String path) {
}
