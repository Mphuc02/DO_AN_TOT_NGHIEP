package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotPermissionException extends RuntimeException{
    public NotPermissionException(String message) {
        super(message);
    }
}