package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class DuplicateException extends RuntimeException{
    private List<? extends Object> data;
    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(List<? extends Object> data, String message){
        super(message);
        this.data = data;
    }
}