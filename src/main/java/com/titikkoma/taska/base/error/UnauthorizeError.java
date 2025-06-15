package com.titikkoma.taska.base.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizeError extends ResponseStatusException {
    public UnauthorizeError(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}