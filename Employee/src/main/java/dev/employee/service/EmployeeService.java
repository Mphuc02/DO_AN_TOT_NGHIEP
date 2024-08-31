package dev.employee.service;

import com.google.gson.Gson;
import dev.common.dto.request.CommonRegisterAccountRequest;
import dev.employee.constant.KafkaConstrant.*;
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
    private final Gson gson;

    @Value(TOPICS.CREATE_ACCOUNT_TOPIC)
    private String CREATE_ACCOUNT_TOPIC;


    public void save(CreateEmployeeRequest request){
//        CommonRegisterAccountRequest registerRequest = employeeUtil.createRegisterAccountRequest(request);
//        kafkaTemplate.send(CREATE_ACCOUNT_TOPIC, gson.toJson(registerRequest));

        Employee entity = employeeUtil.createRequestToEntity(request);
        employeeRepository.save(entity);
    }
}