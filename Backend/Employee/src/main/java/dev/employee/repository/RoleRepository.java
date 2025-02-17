package dev.employee.repository;

import dev.common.model.Role;
import dev.employee.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<EmployeeRole, UUID> {
    boolean existsByRoleAndEmployeeId(Role role, UUID employeeId);
    EmployeeRole findByRoleAndEmployeeId(Role role, UUID employeeId);
    List<EmployeeRole> getAllByEmployeeId(UUID employeeId);
}