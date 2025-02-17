package dev.common.dto.response.error;

import dev.common.exception.BaseException;
import dev.common.model.ErrorField;
import lombok.*;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
    private Map<String, String> fields;
    private String message;

    public static ErrorResponse buildFromBaseException(BaseException ex){
        return new ErrorResponse(ex.getFields().stream().collect(Collectors.toMap(ErrorField::getField, ErrorField::getDetail)),
                ex.getMessage());
    }
}