package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class RetweetConflictException extends TwitterException {
    public RetweetConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
