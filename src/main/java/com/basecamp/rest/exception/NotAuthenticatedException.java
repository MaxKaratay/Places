package com.basecamp.rest.exception;

import org.springframework.security.core.AuthenticationException;

public class NotAuthenticatedException extends AuthenticationException {
    public NotAuthenticatedException(Exception e) {
        super(e.getMessage());
    }
}
