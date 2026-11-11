package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class LikeConflictException extends TwitterException {
    public LikeConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
