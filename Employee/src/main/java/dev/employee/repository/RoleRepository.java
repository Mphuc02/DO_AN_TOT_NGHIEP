package dev.employee.repository;

import dev.common.model.Permission;
import dev.employee.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<EmployeeRole, UUID> {
    boolean existsByPermissionAndEmployeeId(Permission permission, UUID employeeId);
    EmployeeRole findByPermissionAndEmployeeId(Permission permission, UUID employeeId);
    List<EmployeeRole> getAllByEmployeeId(UUID employeeId);
}