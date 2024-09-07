package dev.greeting.service;

import dev.common.dto.response.ExaminationFormResponse;
import dev.common.model.AuthenticatedUser;
import dev.greeting.constant.KafkaConstant;
import dev.greeting.dto.request.CreateFormWithoutAppointmentRequest;
import dev.greeting.entity.ExaminationForm;
import dev.greeting.repository.ExaminationFormRepository;
import dev.greeting.util.ExaminationFormUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExaminationFormService {
    private final ExaminationFormRepository examinationFormRepository;
    private final TicketService ticketService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ExaminationFormUtil examinationFormUtil;

    @Value(KafkaConstant.CREATE_PATIENT_FROM_GREETING)
    private String CREATE_PATIENT_FROM_GREETING;

    public ExaminationFormResponse saveWithoutAppointment(CreateFormWithoutAppointmentRequest request){
        ExaminationForm entity = examinationFormUtil.createRequestToEntity(request);
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        entity.setEmployeeId(employee.getEmployeeId());
        entity = examinationFormRepository.save(entity);

        request.getPatient().setId(UUID.randomUUID());
        kafkaTemplate.send(CREATE_PATIENT_FROM_GREETING, request.getPatient());

        return examinationFormUtil.entityToResponse(entity);
    }
}