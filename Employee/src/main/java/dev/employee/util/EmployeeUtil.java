package dev.employee.util;

import dev.common.dto.request.CommonRegisterAccountRequest;
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
                .dateOfBirth(request.getDateOfBirth())
                .build();
    }

//    public CommonRegisterAccountRequest createRegisterAccountRequest(CreateEmployeeRequest request){
//        return CommonRegisterAccountRequest.builder()
//                .email(request.getEmail())
//                .numberPhone(request.getNumberPhone())
//                .userName()
//                .passWord()
//                .build();
//    }
}