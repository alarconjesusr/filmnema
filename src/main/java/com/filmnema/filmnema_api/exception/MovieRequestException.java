package com.filmnema.filmnema_api.exception;

public class MovieRequestException extends RuntimeException {

    public MovieRequestException(String message) {
        super(message);
    }
}