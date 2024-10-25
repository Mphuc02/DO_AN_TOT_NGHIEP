package dev.employee.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.common.model.Permission;
import dev.employee.dto.request.UpdateEmployeeRequest;
import dev.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(EMPLOYEE_URL.URL)
@RequiredArgsConstructor
public class EmployeeRest {
    private final EmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestParam(value = "permission", required = false) Permission permission){
        return ResponseEntity.ok(employeeService.getByPermisstion(permission));
    }

    @PutMapping(EMPLOYEE_URL.ID)
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @Valid @RequestBody UpdateEmployeeRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EMPLOYEE_EXCEPTION.FAIL_VALIDATION_EMPLOYEE);
        }

        employeeService.update(request, id);
        return ResponseEntity.ok("");
    }
}