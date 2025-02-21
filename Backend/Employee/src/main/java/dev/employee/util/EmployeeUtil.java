package dev.employee.util;

import dev.common.dto.request.RegisterEmployeeCommonRequest;
import dev.common.dto.response.employee.EmployeeResponse;
import dev.common.model.Role;
import dev.employee.dto.request.UpdateEmployeeRequest;
import dev.employee.entity.Employee;
import dev.employee.entity.EmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeUtil {
    private final FullNameUtil fullNameUtil;
    public Employee createRequestToEntity(RegisterEmployeeCommonRequest request){
        Employee employee = Employee.builder()
                .id(request.getId())
                .introduce(request.getIntroduce())
                .dateOfBirth(new Date(request.getDateOfBirth().getTime()))
                .build();

        Set<EmployeeRole> roles = request.getRoles().stream()
                                            .map(permission -> EmployeeRole.builder()
                                                                    .role(permission)
                                                                    .employee(employee)
                                                                    .build())
                                            .collect(Collectors.toSet());
        employee.setRoles(roles);
        employee.setFullName(fullNameUtil.createRequestToEntity(request.getFullName(), employee));
        return employee;
    }

    public EmployeeResponse entityToResponse(Employee entity){
        Set<Role> roles = entity.getRoles()
                                            .stream()
                                            .map(EmployeeRole::getRole)
                                            .collect(Collectors.toSet());
        return EmployeeResponse.builder()
                .id(entity.getId())
                .introduce(entity.getIntroduce())
                .date(entity.getDateOfBirth())
                .fullName(fullNameUtil.entityToResponse(entity.getFullName()))
                .roles(roles)
                .build();
    }

    public List<EmployeeResponse> listEntitiesToResponses(List<Employee> entities){
        return entities.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public void updateRequestToEntity(UpdateEmployeeRequest request, Employee entity){
        if(!ObjectUtils.isEmpty(request.getIntroduce()))
            entity.setIntroduce(request.getIntroduce());

        if(!ObjectUtils.isEmpty(request.getDateOfBirth()))
            entity.setDateOfBirth(new Date(request.getDateOfBirth().getTime()));
    }
}