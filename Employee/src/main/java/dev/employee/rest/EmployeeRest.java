package dev.employee.rest;

import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.employee.dto.request.CreateEmployeeRequest;
import dev.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EMPLOYEE_URL.URL)
@RequiredArgsConstructor
public class EmployeeRest {
    private final EmployeeService employeeService;

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody CreateEmployeeRequest request, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(),
                                                     EMPLOYEE_EXCEPTION.FAIL_VALIDATION_EMPLOYEE);
        }
        employeeService.save(request);
        return ResponseEntity.ok("");
    }
}