package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class TwitterNotFoundException extends TwitterException{
    public TwitterNotFoundException(String message ) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
