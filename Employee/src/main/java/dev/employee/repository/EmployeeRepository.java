package dev.employee.repository;

import dev.common.model.Role;
import dev.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("""
        Select e from EmployeeRole er
        join Employee e on er.employee.id = e.id
        where (:role is null or er.role = :role)
    """)
    List<Employee> getByPermission(Role role);

    List<Employee> findByIdIn(List<UUID> ids);
}
