package dev.employee.util;

import dev.employee.dto.request.CreateEmployeeRequest;
import dev.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeUtil {
    private final FullNameUtil fullNameUtil;
    public Employee createRequestToEntity(CreateEmployeeRequest request){
        return Employee.builder()
                .fullName(fullNameUtil.createRequestToEntity(request.getFullName()))
                .introduce(request.getIntroduce())
                .build();
    }
}