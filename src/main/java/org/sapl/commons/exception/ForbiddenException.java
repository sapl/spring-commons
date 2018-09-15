package org.sapl.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        this("Forbidden, action is not allowed");
    }

    public ForbiddenException(String message) {
        super(message);
    }

}