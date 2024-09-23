package dev.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ObjectIllegalArgumentException extends RuntimeException{
    private final Map<Object, Object> errors;
    public ObjectIllegalArgumentException(Map<Object, Object> errors, String message){
        super(message);
        this.errors = errors;
    }
    public ObjectIllegalArgumentException(List<ObjectError> objectErrors, String message){
        super(message);
        errors = new HashMap<>();
        objectErrors.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
    }
}