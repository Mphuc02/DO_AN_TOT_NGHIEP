package dev.employee.util;

import dev.common.dto.request.CreateFullNameRequest;
import dev.common.dto.response.FullNameResponse;
import dev.employee.entity.Employee;
import dev.employee.entity.FullName;
import org.springframework.stereotype.Component;

@Component
public class FullNameUtil {
    public FullName createRequestToEntity(CreateFullNameRequest request, Employee employee){
        String firstName = request.getFirstName();
        String middleName = request.getMiddleName();
        String lastName = request.getLastName();

        return FullName.builder()
                .firstName(firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase())
                .middleName(middleName.substring(0,1).toUpperCase() + middleName.substring(1).toLowerCase())
                .lastName(lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase())
                .employee(employee)
                .build();
    }

    public FullNameResponse entityToResponse(FullName entity){
        return FullNameResponse.builder()
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .build();
    }
}