package com.dev.phosell.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle validation failures on @RequestBody and DTOs
    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ValidationError(
                        err.getField(),
                        err.getRejectedValue(),
                        err.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setTitle("Validation Failed");
        detail.setDetail("One or more fields have errors. Check 'errors' for details.");
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("errors", errors);
        detail.setProperty("code", "VALIDATION_ERROR");

        return new org.springframework.http.ResponseEntity<>(detail, headers, status);
    }

    // JSON parse errors
    @Override
    protected org.springframework.http.ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setTitle("Malformed JSON Request");
        detail.setDetail(ex.getMostSpecificCause().getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "MALFORMED_JSON");

        log.warn("Malformed JSON: {}", ex.getMostSpecificCause().getMessage());
        return new org.springframework.http.ResponseEntity<>(detail, headers, status);
    }

    // Missing parameters
    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setTitle("Missing Request Parameter");
        detail.setDetail(String.format("Parameter '%s' is missing", ex.getParameterName()));
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "MISSING_PARAMETER");

        return new org.springframework.http.ResponseEntity<>(detail, headers, status);
    }

    // Constraint violations on @RequestParam, @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    protected org.springframework.http.ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        List<ValidationError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Validation Failed");
        detail.setDetail("Request parameters failed validation. See 'errors'.");
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("errors", errors);
        detail.setProperty("code", "PARAM_VALIDATION_ERROR");

        return org.springframework.http.ResponseEntity.badRequest().body(detail);
    }

    // Database constraint / integrity errors
    @ExceptionHandler({DataIntegrityViolationException.class, TransactionSystemException.class})
    protected org.springframework.http.ResponseEntity<ProblemDetail> handleDataIntegrity(
            Exception ex,
            WebRequest request) {

        Throwable root = (ex.getCause() != null) ? ex.getCause() : ex;
        String message = root.getMessage();
        log.error("Data integrity violation: {}", message);

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("Data Integrity Violation");
        detail.setDetail("A database error occurred. Please check constraints.");
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "DB_CONSTRAINT_ERROR");

        return new org.springframework.http.ResponseEntity<>(detail, HttpStatus.CONFLICT);
    }

    // Security: access denied
    @ExceptionHandler(AccessDeniedException.class)
    protected org.springframework.http.ResponseEntity<ProblemDetail> handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request) {

        log.warn("Access denied: {}", ex.getMessage());
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("Forbidden");
        detail.setDetail("You do not have permission to access this resource.");
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "ACCESS_DENIED");

        return org.springframework.http.ResponseEntity.status(HttpStatus.FORBIDDEN).body(detail);
    }

    // Security: authentication failure
    @ExceptionHandler(AuthenticationException.class)
    protected org.springframework.http.ResponseEntity<ProblemDetail> handleAuthFailure(
            AuthenticationException ex,
            WebRequest request) {

        log.warn("Authentication failed: {}", ex.getMessage());
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle("Unauthorized");
        detail.setDetail("Authentication is required to access this resource.");
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "UNAUTHENTICATED");

        return org.springframework.http.ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detail);
    }

    // Catch-all for other exceptions
    @ExceptionHandler(Exception.class)
    protected org.springframework.http.ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex,
            WebRequest request) {

        String errorId = UUID.randomUUID().toString();
        String msg = ex.getMessage() != null ? ex.getMessage() : "No message available";
        log.error("Unexpected error [{}]: {}", errorId, msg, ex);

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle("Internal Server Error");
        detail.setDetail("An unexpected error occurred. Reference ID: " + errorId);
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "INTERNAL_ERROR");

        return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(detail);
    }

    // DTO for validation errors
    public static record ValidationError(String field, Object rejectedValue, String message) {}
}
