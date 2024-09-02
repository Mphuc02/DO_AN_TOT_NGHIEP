package dev.employee.service;

import com.google.gson.Gson;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.response.EmployeeResponse;
import dev.employee.entity.Employee;
import dev.employee.repository.EmployeeRepository;
import dev.employee.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeUtil employeeUtil;
    private final Gson gson;

    public List<EmployeeResponse> getAll(){
        return employeeUtil.listEntitiesToResponses(employeeRepository.findAll());
    }

    @KafkaListener(topics = "${kafka.topics.create-employee-topic}", groupId = "${kafka.group-id.account}")
//        public void save(ConsumerRecord<String, Object> request){ Way1
    public void save(@Payload CommonRegisterEmployeeRequest request){
        log.info(String.format("Receive request save Employee from Kafka with value: %s", gson.toJson(request)));
        Employee entity = employeeUtil.createRequestToEntity(request);
        employeeRepository.save(entity);
    }
}