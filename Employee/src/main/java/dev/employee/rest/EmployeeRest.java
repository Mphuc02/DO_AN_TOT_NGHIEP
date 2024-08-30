package dev.employee.rest;

import dev.common.constant.ApiConstant.*;
import dev.employee.dto.request.CreateEmployeeRequest;
import dev.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EMPLOYEE_URL.URL)
@RequiredArgsConstructor
public class EmployeeRest {
    private final EmployeeService employeeService;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody CreateEmployeeRequest request){
        return ResponseEntity.ok(employeeService.save(request));
    }
}