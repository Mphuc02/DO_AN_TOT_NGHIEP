package dev.employee.util;

import dev.employee.dto.request.CreateFullNameRequest;
import dev.employee.entity.FullName;
import org.springframework.stereotype.Component;

@Component
public class FullNameUtil {
    public FullName createRequestToEntity(CreateFullNameRequest request){
        return FullName.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .build();
    }
}