package dev.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.crossstore.ChangeSetPersister;
import java.util.List;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException{
    private List<? extends Object> data;
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(List<? extends Object> data, String message){
        super(message);
        this.data = data;
    }
}