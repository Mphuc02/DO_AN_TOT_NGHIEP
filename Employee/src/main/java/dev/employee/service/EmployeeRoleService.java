package dev.employee.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.DuplicateException;
import dev.common.exception.NotFoundException;
import dev.common.model.Permission;
import dev.employee.dto.request.UpdateEmployeeRoleRequest;
import dev.employee.entity.Employee;
import dev.employee.entity.EmployeeRole;
import dev.employee.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeRoleService {
    private final RoleRepository roleRepository;

    public List<Permission> getAllRolesOfEmployeeById(UUID employeeId){
        return roleRepository.getAllByEmployeeId(employeeId)
                        .stream()
                        .map(EmployeeRole::getPermission)
                        .collect(Collectors.toList());
    }

    @Transactional
    public void addRolesForEmployee(List<UpdateEmployeeRoleRequest> requests, Employee employee){
        if(ObjectUtils.isEmpty(requests))
            return;

        List<Permission> existedRole = new ArrayList<>();
        List<EmployeeRole> roles = new ArrayList<>();

        requests.forEach(request -> {
            if(roleRepository.existsByPermissionAndEmployeeId(request.getPermission(), employee.getId()) ){
                existedRole.add(request.getPermission());
                return;
            }

            EmployeeRole role = EmployeeRole.builder()
                    .permission(request.getPermission())
                    .employee(employee)
                    .build();
            roles.add(role);
        });

        if(!ObjectUtils.isEmpty(existedRole)){
            throw new DuplicateException(existedRole, EMPLOYEE_EXCEPTION.EMPLOYEE_HAD_ROLES);
        }
        roleRepository.saveAll(roles);
    }

    @Transactional
    public void deleteRolesForEmployee(List<UpdateEmployeeRoleRequest> requests, UUID employeeId){
        List<EmployeeRole> deleteList = new ArrayList<>();
        List<Permission> idsNotFound = new ArrayList<>();

        requests.forEach(request -> {
            EmployeeRole role = roleRepository.findByPermissionAndEmployeeId(request.getPermission(), employeeId);
            if(ObjectUtils.isEmpty(role))
                idsNotFound.add(request.getPermission());
            else
                deleteList.add(role);
        });

        if(!ObjectUtils.isEmpty(idsNotFound)){
            throw new NotFoundException(idsNotFound, EMPLOYEE_EXCEPTION.EMPLOYEE_DID_NOT_HAD_ROLES);
        }

        roleRepository.deleteAll(deleteList);
    }
}