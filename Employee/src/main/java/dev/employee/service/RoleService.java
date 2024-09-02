package dev.employee.service;

import dev.common.model.Permission;
import dev.employee.entity.EmployeeRole;
import dev.employee.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public List<Permission> getAllRolesOfEmployeeById(UUID employeeId){
        return repository.getAllByEmployeeId(employeeId)
                        .stream()
                        .map(EmployeeRole::getPermission)
                        .collect(Collectors.toList());
    }
}