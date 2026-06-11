package com.filmnema.filmnema_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more fields are invalid.");
        problemDetail.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList());
        problemDetail.setProperty("code", "VALIDATION_ERROR");

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more request parameters are invalid.");
        problemDetail.setProperty("errors", exception.getConstraintViolations().stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList());
        problemDetail.setProperty("code", "VALIDATION_ERROR");

        return problemDetail;
    }

    @ExceptionHandler(MovieRequestException.class)
    public ProblemDetail handleMovieRequest(MovieRequestException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad request");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("code", "MOVIE_BAD_REQUEST");

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An unexpected error occurred.");
        problemDetail.setProperty("code", "INTERNAL_SERVER_ERROR");

        return problemDetail;
    }

    private record ValidationError(String field, String message) {
    }
}