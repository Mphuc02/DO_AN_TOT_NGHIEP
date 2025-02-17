package dev.common.exception;

import dev.common.model.ErrorField;
import lombok.*;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private final List<ErrorField> fields;
    private final String message;
    private final HttpStatus httpStatus;

    public static BaseExceptionBuilder buildBadRequest(){
        return new BaseExceptionBuilder(HttpStatus.BAD_REQUEST);
    }

    public static BaseExceptionBuilder buildNotFound(){
        return new BaseExceptionBuilder(HttpStatus.NOT_FOUND);
    }

    public static class BaseExceptionBuilder{
        private List<ErrorField> fields = new ArrayList<>();
        private String message;
        private HttpStatus httpStatus;

        public BaseExceptionBuilder(HttpStatus httpStatus){
            this.httpStatus = httpStatus;
        }

        public BaseExceptionBuilder addFields(List<ErrorField> fields){
            this.fields = fields;
            return this;
        }

        public BaseExceptionBuilder addField(ErrorField field){
            this.fields.add(field);
            return this;
        }

        public BaseExceptionBuilder message(String message){
            this.message = message;
            return this;
        }

        public BaseExceptionBuilder httpStatus(HttpStatus httpStatus){
            this.httpStatus = httpStatus;
            return this;
        }

        public BaseException build(){
            return new BaseException(fields, message, httpStatus);
        }
    }
}