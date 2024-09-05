package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}