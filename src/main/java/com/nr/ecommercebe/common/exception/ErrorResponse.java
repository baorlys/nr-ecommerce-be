package com.nr.ecommercebe.common.exception;

import java.time.LocalDateTime;


public record ErrorResponse(LocalDateTime timestamp, int status, String message, String path) {
}
