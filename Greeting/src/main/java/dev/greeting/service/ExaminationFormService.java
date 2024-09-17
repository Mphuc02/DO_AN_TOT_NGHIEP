package dev.greeting.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.dto.response.ExaminationFormResponse;
import dev.common.model.AuthenticatedUser;
import dev.greeting.dto.request.CreateFormWithoutAppointmentRequest;
import dev.greeting.entity.ExaminationForm;
import dev.greeting.repository.ExaminationFormRepository;
import dev.greeting.util.ExaminationFormUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExaminationFormService {
    private final ExaminationFormRepository examinationFormRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ExaminationFormUtil examinationFormUtil;

    @Value(CREATE_PATIENT_FROM_GREETING_TOPIC)
    private String CREATE_PATIENT_TOPIC;

    @Value(CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC)
    private String CREATE_EXAMINATION_RESULT_TOPIC;

    public ExaminationFormResponse saveWithoutAppointment(CreateFormWithoutAppointmentRequest request){
        ExaminationForm entity = examinationFormUtil.createRequestToEntity(request);
        request.getPatient().setExaminationFormID(entity.getId());
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        entity.setEmployeeId(employee.getId());
        entity = examinationFormRepository.save(entity);

        kafkaTemplate.send(CREATE_PATIENT_TOPIC, request.getPatient());
        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, examinationFormUtil.buildCreateExaminationResultRequest(entity));
        return examinationFormUtil.entityToResponse(entity);
    }
}