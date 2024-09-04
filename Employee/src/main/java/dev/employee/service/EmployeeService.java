package dev.employee.service;

import com.google.gson.Gson;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.response.EmployeeResponse;
import dev.common.exception.NotFoundException;
import dev.employee.dto.request.UpdateEmployeeRequest;
import dev.employee.entity.Employee;
import dev.employee.repository.EmployeeRepository;
import dev.employee.util.EmployeeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeUtil employeeUtil;
    private final Gson gson;
    private final EmployeeRoleService employeeRoleService;
    private final FullNameService fullNameService;

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

    @Transactional
    public void update(UpdateEmployeeRequest request, UUID id){
        Employee findToUpdate = findById(id);

        if(!ObjectUtils.isEmpty(request.getAddRoles())){
            employeeRoleService.addRolesForEmployee(request.getAddRoles(), findToUpdate);
        }

        if(!ObjectUtils.isEmpty(request.getDeleteRoles())){
            employeeRoleService.deleteRolesForEmployee(request.getDeleteRoles(), findToUpdate.getId());
        }

        if(!ObjectUtils.isEmpty(request.getFullName())){
            fullNameService.update(request.getFullName());
        }

        employeeUtil.updateRequestToEntity(request, findToUpdate);
        employeeRepository.save(findToUpdate);
    }

    public Employee findById(UUID id){
        return employeeRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(EMPLOYEE_EXCEPTION.EMPLOYEE_NOT_FOUND));
    }
}