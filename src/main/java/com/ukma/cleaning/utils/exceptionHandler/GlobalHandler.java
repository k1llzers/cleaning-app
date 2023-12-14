package com.ukma.cleaning.utils.exceptionHandler;


import com.ukma.cleaning.utils.exceptions.*;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        log.info("Validation on controller failed: {}", message);
        return new ResponseEntity<>(formatMessage(message), getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailDuplicateException.class, PhoneNumberDuplicateException.class, ProposalNameDuplicateException.class})
    public ResponseEntity<String> handleDuplicateException(Exception e) {
        return new ResponseEntity<>(formatMessage(e.getMessage()), getHttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleNoSuchEntityException(AccessDeniedException e) {
        return new ResponseEntity<>(formatMessage(e.getMessage()), getHttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityException e) {
        return new ResponseEntity<>(formatMessage(e.getMessage()), getHttpHeaders(), HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler
//    public ResponseEntity<String> handleException(Exception e) {
//        return new ResponseEntity<>(formatMessage(e.getMessage()), getHttpHeaders(), HttpStatus.BAD_REQUEST);
//    }

    private static String formatMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"errorMessage\":\"")
                .append(message)
                .append("\"}");
        return sb.toString();
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
