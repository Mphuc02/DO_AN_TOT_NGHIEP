package dev.common.dto.response;

import dev.common.exception.BaseException;
import dev.common.model.ErrorField;
import lombok.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private Map<String, String> fields;
    private String message;

    public static ErrorResponseDTO buildFromBaseException(BaseException ex){
        return new ErrorResponseDTO(ex.getFields().stream().collect(Collectors.toMap(ErrorField::getField, ErrorField::getDetail)),
                ex.getMessage());
    }
}