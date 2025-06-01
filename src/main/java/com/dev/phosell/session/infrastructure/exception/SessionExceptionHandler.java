package com.dev.phosell.session.infrastructure.exception;

import com.dev.phosell.session.domain.exception.photographer.NotAvailablePhotographerException;
import com.dev.phosell.session.domain.exception.session.InvalidSessionValueException;
import com.dev.phosell.session.domain.exception.slot.*;
import com.dev.phosell.session.infrastructure.adapter.in.SessionController;
import com.dev.phosell.sessionpackage.application.exception.SessionPackageNotFoundExcaption;
import com.dev.phosell.user.application.exception.UserExistsException;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = SessionController.class )
public class SessionExceptionHandler {

    //photographer
    @ExceptionHandler(NotAvailablePhotographerException.class)
    public ResponseEntity<ProblemDetail> handleNotAvailablePhotographer(
            NotAvailablePhotographerException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("Not Available Photographer");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "NOT_AVAILABLE_PHOTOGRAPHER");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    //session
    @ExceptionHandler(InvalidSessionValueException.class)
    public ResponseEntity<ProblemDetail> handleInvalidSessionValue(
            InvalidSessionValueException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Session Value");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "INVALID_SESSION_VALUE");
        detail.setProperty("param",ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    //slots
    @ExceptionHandler(InvalidBookingHourException.class)
    public ResponseEntity<ProblemDetail> handleInvalidBookingHour(
            InvalidBookingHourException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Booking Hour");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "INVALID_BOOKING_HOUR");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSessionDateException.class)
    public ResponseEntity<ProblemDetail> handleInvalidSessionDateException(
            InvalidSessionDateException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Session Date");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "INVALID_SESSION_DATE");
        detail.setProperty("param",ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSessionSlotException.class)
    public ResponseEntity<ProblemDetail> handleInvalidSessionSlotException(
            InvalidSessionSlotException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Session Slot");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "INVALID_SESSION_SLOT");
        detail.setProperty("param",ex.getValue());

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionSlotAlreadyExpiredException.class)
    public ResponseEntity<ProblemDetail> handleSessionSlotAlreadyExpired(
            SessionSlotAlreadyExpiredException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Session Slot Already Expired");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "SESSION_SLOT_ALREADY_EXPIRED");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionSlotOutOfWorkingHourException.class)
    public ResponseEntity<ProblemDetail> handleSessionSlotOutOfWorkingHour(
            SessionSlotOutOfWorkingHourException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Session Slot Out Of Working Hour");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "SESSION_SLOT_OUT_OF_WORKING_HOUR");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("User Not Found");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "USER_NOT_FOUND");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserExist(
            UserExistsException ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("User Already Exist");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "USER_ALREADY_EXIST");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SessionPackageNotFoundExcaption.class)
    public ResponseEntity<ProblemDetail> handleSessionPackageNotFound(
            SessionPackageNotFoundExcaption ex, WebRequest request){

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Session Package Not Found");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        detail.setProperty("code", "SESSION_PACKAGE_NOT_FOUND");

        return new ResponseEntity<>(detail, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
