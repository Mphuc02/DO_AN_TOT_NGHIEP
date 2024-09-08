package dev.greeting.service;

import dev.common.client.InternalAccountClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.ExaminationFormResponse;
import dev.common.exception.DuplicateException;
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
    private final InternalAccountClient internalAccountClient;

    @Value(KafkaConstant.CREATE_PATIENT_FROM_GREETING)
    private String CREATE_PATIENT_FROM_GREETING;

    public ExaminationFormResponse saveWithoutAppointment(CreateFormWithoutAppointmentRequest request){
        request.getPatient().setId(UUID.randomUUID());
        if(!internalAccountClient.saveAccountFrommGreeting(request.getPatient()))
            throw new DuplicateException(AUTHENTICATION_EXCEPTION.NUMBER_PHONE_EXISTED);

        ExaminationForm entity = examinationFormUtil.createRequestToEntity(request);
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        entity.setEmployeeId(employee.getEmployeeId());
        entity = examinationFormRepository.save(entity);

        kafkaTemplate.send(CREATE_PATIENT_FROM_GREETING, request.getPatient());

        return examinationFormUtil.entityToResponse(entity);
    }
}