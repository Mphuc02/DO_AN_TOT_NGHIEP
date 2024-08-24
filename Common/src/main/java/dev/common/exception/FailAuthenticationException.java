package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FailAuthenticationException extends RuntimeException{
    public FailAuthenticationException(String message) {
        super(message);
    }
}