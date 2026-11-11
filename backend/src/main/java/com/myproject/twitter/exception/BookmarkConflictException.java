package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class BookmarkConflictException extends TwitterException {
    public BookmarkConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
