package dev.employee.util;

import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.response.EmployeeResponse;
import dev.common.model.Permission;
import dev.employee.entity.Employee;
import dev.employee.entity.EmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeUtil {
    private final FullNameUtil fullNameUtil;
    public Employee createRequestToEntity(CommonRegisterEmployeeRequest request){
        Set<EmployeeRole> roles = request.getPermissions().stream()
                                            .map(permission -> EmployeeRole.builder()
                                                                    .permission(permission)
                                                                    .build())
                                            .collect(Collectors.toSet());

        return Employee.builder()
                .id(request.getId())
                .fullName(fullNameUtil.createRequestToEntity(request.getFullName()))
                .introduce(request.getIntroduce())
                .dateOfBirth(new Date(request.getDateOfBirth().getTime()))
                .roles(roles)
                .build();
    }

    public EmployeeResponse entityToResponse(Employee entity){
        Set<Permission> permissions = entity.getRoles()
                                            .stream()
                                            .map(EmployeeRole::getPermission)
                                            .collect(Collectors.toSet());
        return EmployeeResponse.builder()
                .introduce(entity.getIntroduce())
                .date(entity.getDateOfBirth())
                .fullName(fullNameUtil.entityToResponse(entity.getFullName()))
                .permissions(permissions)
                .build();
    }

    public List<EmployeeResponse> listEntitiesToResponses(List<Employee> entities){
        return entities.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}