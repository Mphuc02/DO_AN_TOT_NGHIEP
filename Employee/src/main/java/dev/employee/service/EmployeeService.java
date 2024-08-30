package dev.employee.service;

import dev.common.dto.response.EmployeeResponse;
import dev.employee.dto.request.CreateEmployeeRequest;
import dev.employee.entity.Employee;
import dev.employee.repository.EmployeeRepository;
import dev.employee.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeUtil employeeUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topics.create-acccount-topic}")
    private String CREATE_ACCOUNT_TOPIC;

    public EmployeeResponse save(CreateEmployeeRequest request){
        Employee entity = employeeUtil.createRequestToEntity(request);
        kafkaTemplate.send(CREATE_ACCOUNT_TOPIC, "hello may me");
        return null;
    }
}