package dev.employee.rest;

import dev.common.constant.ApiConstant.*;
import dev.employee.service.EmployeeRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping(EMPLOYEE_URL.ROLE_URL)
@RequiredArgsConstructor
public class RoleRest {
    private final EmployeeRoleService employeeRoleService;

    @GetMapping(EMPLOYEE_URL.ID)
    public ResponseEntity<Object> getAllRolesOfEmployee(@PathVariable UUID id){
        return ResponseEntity.ok(employeeRoleService.getAllRolesOfEmployeeById(id));
    }
}
