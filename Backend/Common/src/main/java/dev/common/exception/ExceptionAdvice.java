package dev.common.exception;

import dev.common.dto.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handler(JwtException ex){
        log.error("Exception when validate jwt", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handler(BadRequestException ex){
        log.error("Bad request exception", ex);
        Map<String, Object> errors = Map.of("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handler(MethodArgumentNotValidException ex){
        log.error("Exception when validate object: " + ex.getObjectName());
        HashMap<String, String> errors = new HashMap<>();
        for(ObjectError error: ex.getAllErrors()){
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(errors, "Lỗi khi kiểm tra thông tin"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handle(BaseException ex){
        log.error("Exception with base Exception", ex);
        return new ResponseEntity<>(ErrorResponse.buildFromBaseException(ex), ex.getHttpStatus());
    }
}