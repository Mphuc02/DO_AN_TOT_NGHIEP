package dev.employee.service;

import dev.common.dto.response.EmployeeResponse;
import dev.employee.dto.request.CreateEmployeeRequest;
import dev.employee.entity.Employee;
import dev.employee.repository.EmployeeRepository;
import dev.employee.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeUtil employeeUtil;

    public EmployeeResponse save(CreateEmployeeRequest request){

        Employee entity = employeeUtil.createRequestToEntity(request);
    }
}