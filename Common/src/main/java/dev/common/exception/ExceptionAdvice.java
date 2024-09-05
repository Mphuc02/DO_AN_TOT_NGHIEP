package dev.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(FailAuthenticationException.class)
    public ResponseEntity<Object> handler(FailAuthenticationException ex){
        log.error("Exception when authenticate user", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<Object> handler(NotPermissionException ex){
        log.error("Exception when user doesn't have authorization", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectIllegalArgumentException.class)
    public ResponseEntity<Object> handler(ObjectIllegalArgumentException ex){
        log.error("Exception when receive object request", ex);
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handler(NotFoundException ex){
        log.error("Exception when find entity", ex);
        HashMap<String, Object> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("data", ex.getData());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> handler(DuplicateException ex){
        log.error("Exception for duplicate", ex);
        HashMap<String, Object> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("data", ex.getData());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}