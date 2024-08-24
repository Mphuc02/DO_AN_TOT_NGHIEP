package dev.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(FailAuthenticationException.class)
    public ResponseEntity<Object> handler(FailAuthenticationException ex){
        log.error("Exception when authenticate user", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectIllegalArgumentException.class)
    public ResponseEntity<Object> handler(ObjectIllegalArgumentException ex){
        log.error("Exception when receive object request", ex);
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }
}