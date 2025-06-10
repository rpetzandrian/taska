package com.titikkoma.taska.base.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestError extends ResponseStatusException {
    public BadRequestError(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
