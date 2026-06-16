package com.filmnema.filmnema_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String errorId = newErrorId();
        logger.warn("Validation failed [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more fields are invalid.");
        problemDetail.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList());
        problemDetail.setProperty("code", "VALIDATION_ERROR");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        String errorId = newErrorId();
        logger.warn("Constraint violation [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more request parameters are invalid.");
        problemDetail.setProperty("errors", exception.getConstraintViolations().stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList());
        problemDetail.setProperty("code", "VALIDATION_ERROR");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(FilmnemaException.class)
    public ProblemDetail handleFilmnemaException(FilmnemaException exception) {
        String errorId = newErrorId();
        logger.warn("Domain error [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad request");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("code", "FILMNEMA_BAD_REQUEST");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException exception) {
        String errorId = newErrorId();
        logger.warn("Authentication failed [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail("Invalid username or password.");
        problemDetail.setProperty("code", "AUTHENTICATION_FAILED");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception) {
        String errorId = newErrorId();
        logger.warn("Unsupported media type [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        problemDetail.setTitle("Unsupported media type");
        problemDetail.setDetail("Send the request as application/json.");
        problemDetail.setProperty("code", "UNSUPPORTED_MEDIA_TYPE");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception) {
        String errorId = newErrorId();
        logger.warn("Upload too large [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(413));
        problemDetail.setTitle("File too large");
        problemDetail.setDetail("The uploaded image exceeds the maximum allowed size.");
        problemDetail.setProperty("code", "FILE_TOO_LARGE");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception exception) {
        String errorId = newErrorId();
        logger.error("Unexpected error [{}]", errorId, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An unexpected error occurred.");
        problemDetail.setProperty("code", "INTERNAL_SERVER_ERROR");
        problemDetail.setProperty("errorId", errorId);

        return problemDetail;
    }

    private String newErrorId() {
        return UUID.randomUUID().toString();
    }

    private record ValidationError(String field, String message) {
    }
}