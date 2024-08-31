package dev.employee.util;

import dev.employee.dto.request.CreateFullNameRequest;
import dev.employee.entity.FullName;
import org.springframework.stereotype.Component;

@Component
public class FullNameUtil {
    public FullName createRequestToEntity(CreateFullNameRequest request){
        String firstName = request.getFirstName();
        String middleName = request.getMiddleName();
        String lastName = request.getLastName();

        return FullName.builder()
                .firstName(firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase())
                .middleName(middleName.substring(0,1).toUpperCase() + middleName.substring(1).toLowerCase())
                .lastName(lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase())
                .build();
    }
}