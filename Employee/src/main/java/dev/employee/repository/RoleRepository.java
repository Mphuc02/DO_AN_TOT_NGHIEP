package dev.employee.repository;

import dev.employee.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<EmployeeRole, UUID> {
    List<EmployeeRole> getAllByEmployeeId(UUID employeeId);
}