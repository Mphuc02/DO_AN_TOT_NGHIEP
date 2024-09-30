package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}