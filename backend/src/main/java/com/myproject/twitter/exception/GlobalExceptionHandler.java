package com.myproject.twitter.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TwitterException.class)
    public ResponseEntity<TwitterErrorResponse> handleException(TwitterException twitterException){

        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse(
                twitterException.getMessage(),
                twitterException.getHttpStatus().value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(twitterErrorResponse, twitterException.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<TwitterErrorResponse> handle(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){

        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse(
                methodArgumentTypeMismatchException.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(twitterErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TwitterErrorResponse> handle(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        TwitterErrorResponse error = new TwitterErrorResponse(
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<TwitterErrorResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {

        TwitterErrorResponse error = new TwitterErrorResponse(
                "İşlem yapılamadı: Gönderilen parametreler hatalı veya eksik.",
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<TwitterErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        TwitterErrorResponse error = new TwitterErrorResponse(
                ("Geçersiz argüman hatası: " + ex.getMessage()),
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TwitterErrorResponse> handle(Exception exception){

        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(twitterErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
