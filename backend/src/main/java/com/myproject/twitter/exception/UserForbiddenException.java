package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class UserForbiddenException extends TwitterException {
    public UserForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
