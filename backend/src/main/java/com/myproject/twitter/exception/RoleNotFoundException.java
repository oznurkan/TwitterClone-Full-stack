package com.myproject.twitter.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends TwitterException {

    public RoleNotFoundException(String message) { super(message, HttpStatus.INTERNAL_SERVER_ERROR);}
}
