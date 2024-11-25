package dev.employee.rest;

import static dev.common.constant.ApiConstant.EmployeeUrl.*;

import dev.common.constant.AuthorizationConstant;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.common.model.Role;
import dev.employee.dto.request.UpdateEmployeeRequest;
import dev.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(URL)
@RequiredArgsConstructor
public class EmployeeRest {
    private final EmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestParam(value = "role", required = false) Role role){
        return ResponseEntity.ok(employeeService.getByPermisstion(role));
    }

    @GetMapping(ID)
    public ResponseEntity<Object> findById(@PathVariable UUID id){
        return ResponseEntity.ok(employeeService.findDetailById(id));
    }

    @PutMapping(ID)
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @Valid @RequestBody UpdateEmployeeRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EMPLOYEE_EXCEPTION.FAIL_VALIDATION_EMPLOYEE);
        }

        employeeService.update(request, id);
        return ResponseEntity.ok("");
    }

    @PostMapping(FIND_BY_IDS)
    public ResponseEntity<Object> findByIds(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(employeeService.findByIds(ids));
    }

    @GetMapping(GET_LOGGED_USER_INFORMATION)
    @PreAuthorize(AuthorizationConstant.DOCTOR)
    public ResponseEntity<Object> getLoggedUserInformation(){
        return ResponseEntity.ok(employeeService.getLoggedUserInformation());
    }
}