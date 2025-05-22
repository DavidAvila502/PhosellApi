package com.dev.phosell.authentication.infrastructure.exception;

import com.dev.phosell.authentication.domain.exception.InvalidRefreshTokenException;
import com.dev.phosell.authentication.infrastructure.adapter.in.AuthenticationController;
import com.dev.phosell.user.application.exception.UserExistsException;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = AuthenticationController.class)
public class AuthenticationExceptionHandler {

    @ExceptionHandler(InvalidUserOrPasswordException.class)
    public ResponseEntity<ProblemDetail> handleInvalidUserOrPassword(
            InvalidUserOrPasswordException ex,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle("Invalid Username Or Password");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "USERNAME_OR_PASSWORD_INVALID");
//        detail.setProperty("token", ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ProblemDetail> handleRefreshTokenNotFound(
            InvalidRefreshTokenException ex,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle("Unauthorized");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "REFRESHTOKEN_INVALID");
//        detail.setProperty("token", ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(
            UserNotFoundException ex,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("User Not Found");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "USER_NOT_FOUND");
        detail.setProperty("param", ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserExists(
            UserExistsException ex,
            WebRequest request) {

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("User Already Exists");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "USER_ALREADY_EXISTS");
        detail.setProperty("email", ex.getEmail());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.CONFLICT);
    }


    // Generic
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ProblemDetail> handleUserGeneric(
//            Exception ex,
//            WebRequest request) {
//
//        String errorId = java.util.UUID.randomUUID().toString();
//        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        detail.setTitle("Internal Authentication Module Error");
//        detail.setDetail("Unexpected error in Authentication module. Ref: " + errorId);
//        detail.setProperty("timestamp", LocalDateTime.now());
//        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
//        detail.setProperty("code", "AUTHENTICATION_MODULE_ERROR");
//        detail.setProperty("errorId", errorId);
//
//         log.error("Error [{}] in User module", errorId, ex);
//
//        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
