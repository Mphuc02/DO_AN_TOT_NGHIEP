package dev.employee.service;

import com.google.gson.Gson;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.response.user.EmployeeResponse;
import dev.common.exception.NotFoundException;
import dev.common.model.Role;
import dev.common.util.AuditingUtil;
import dev.employee.dto.request.UpdateEmployeeRequest;
import dev.employee.entity.Employee;
import dev.employee.repository.EmployeeRepository;
import dev.employee.util.EmployeeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuditingUtil auditingUtil;

    @Value(KafkaTopicsConstrant.CREATED_EMPLOYEE_TOPIC)
    private String CREATED_EMPLOYEE_TOPIC;

    public List<EmployeeResponse> getByPermisstion(Role role){
        return employeeUtil.listEntitiesToResponses(employeeRepository.getByPermission(role));
    }

    @KafkaListener(topics = "${kafka.topics.create-employee-topic}", groupId = "${kafka.group-id.account}")
//        public void save(ConsumerRecord<String, Object> request){ Way1
    public void save(@Payload CommonRegisterEmployeeRequest request){
        log.info(String.format("Receive request save Employee from Kafka with value: %s", gson.toJson(request)));
        Employee entity = employeeUtil.createRequestToEntity(request);
        employeeRepository.save(entity);
        kafkaTemplate.send(CREATED_EMPLOYEE_TOPIC, request.getOwner());
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

    public EmployeeResponse findDetailById(UUID id){
        Employee employee = findById(id);
        Set<Role> roles = new HashSet<>(employeeRoleService.findAllRolesOfEmployeeById(id));
        EmployeeResponse response = employeeUtil.entityToResponse(employee);
        response.setRoles(roles);
        return response;
    }

    private Employee findById(UUID id){
        return employeeRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(EMPLOYEE_EXCEPTION.EMPLOYEE_NOT_FOUND));
    }

    public List<EmployeeResponse> findByIds(List<UUID> ids){
        List<Employee> employees = employeeRepository.findByIdIn(ids);
        return employeeUtil.listEntitiesToResponses(employees);
    }

    public EmployeeResponse getLoggedUserInformation(){
        UUID userId = auditingUtil.getUserLogged().getId();
        return findDetailById(userId);
    }
}