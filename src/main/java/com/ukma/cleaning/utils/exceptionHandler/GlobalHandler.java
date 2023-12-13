package com.ukma.cleaning.utils.exceptionHandler;


import com.ukma.cleaning.utils.exceptions.EmailDuplicateException;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.exceptions.PhoneNumberDuplicateException;
import com.ukma.cleaning.utils.exceptions.ProposalNameDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
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
        StringBuilder sb = new StringBuilder();
        sb.append("{\"errorMessage\":\"")
                .append(message)
                .append("\"}");
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({EmailDuplicateException.class, PhoneNumberDuplicateException.class, ProposalNameDuplicateException.class})
    public ResponseEntity<String> handleDuplicateException(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"errorMessage\":\"")
                .append(e.getMessage())
                .append("\"}");
        return new ResponseEntity<>(sb.toString(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"errorMessage\":\"")
                .append(e.getMessage())
                .append("\"}");
        return new ResponseEntity<>(sb.toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"errorMessage\":\"")
                .append(e.getMessage())
                .append("\"}");
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
}
