package dev.employee.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.DuplicateException;
import dev.common.exception.NotFoundException;
import dev.common.model.Role;
import dev.employee.dto.request.UpdateEmployeeRoleRequest;
import dev.employee.entity.Employee;
import dev.employee.entity.EmployeeRole;
import dev.employee.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeRoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAll(){
        return Arrays.asList(Role.DOCTOR, Role.RECEPTION_STAFF, Role.MEDICINE_DISPENSER);
    }

    public List<Role> findAllRolesOfEmployeeById(UUID employeeId){
        return roleRepository.getAllByEmployeeId(employeeId)
                        .stream()
                        .map(EmployeeRole::getRole)
                        .toList();
    }

    @Transactional
    public void addRolesForEmployee(List<UpdateEmployeeRoleRequest> requests, Employee employee){
        if(ObjectUtils.isEmpty(requests))
            return;

        List<Role> existedRole = new ArrayList<>();
        List<EmployeeRole> roles = new ArrayList<>();

        requests.forEach(request -> {
            if(roleRepository.existsByRoleAndEmployeeId(request.getRole(), employee.getId()) ){
                existedRole.add(request.getRole());
                return;
            }

            EmployeeRole role = EmployeeRole.builder()
                    .role(request.getRole())
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
        List<Role> idsNotFound = new ArrayList<>();

        requests.forEach(request -> {
            EmployeeRole role = roleRepository.findByRoleAndEmployeeId(request.getRole(), employeeId);
            if(ObjectUtils.isEmpty(role))
                idsNotFound.add(request.getRole());
            else
                deleteList.add(role);
        });

        if(!ObjectUtils.isEmpty(idsNotFound)){
            throw new NotFoundException(idsNotFound, EMPLOYEE_EXCEPTION.EMPLOYEE_DID_NOT_HAD_ROLES);
        }

        roleRepository.deleteAll(deleteList);
    }
}