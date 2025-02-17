package dev.greeting.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.constant.ExceptionConstant.GREETING_EXCEPTION;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.UpdateNumberExaminationFormRequest;
import dev.common.dto.response.examination_form.ExaminationFormResponse;
import dev.common.exception.BaseException;
import dev.common.exception.NotFoundException;
import dev.common.model.AuthenticatedUser;
import dev.common.util.AuditingUtil;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
import dev.greeting.dto.request.CreateFormWithAppointmentRequest;
import dev.greeting.entity.ExaminationForm;
import dev.greeting.repository.ExaminationFormRepository;
import dev.greeting.util.ExaminationFormMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationFormService {
    private final ExaminationFormRepository examinationFormRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ExaminationFormMapperUtil examinationFormMapperUtil;
    private final AuditingUtil auditingUtil;

    @Value(CREATE_PATIENT_ACCOUNT_FROM_GREETING_TOPIC)
    private String CREATE_PATIENT_TOPIC;

    @Value(CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC)
    private String CREATE_EXAMINATION_RESULT_TOPIC;

    @Value(KafkaTopicsConstrant.UPDATED_NUMBER_EXAMINATION_FORM_TOPIC)
    private String UPDATED_NUMBER_EXAMINATION_FORM_TOPIC;

    @Transactional
    public void saveFirstTimePatient(CreateFormForFirstTimePatientRequest request){
        ExaminationForm entity = examinationFormMapperUtil.createRequestWithFirstTimePatientToEntity(request);
        request.getPatient().setExaminationFormID(entity.getId());
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        entity.setEmployeeId(employee.getId());
        entity.setCreatedAt(LocalDateTime.now());
        entity = examinationFormRepository.save(entity);

        request.getPatient().setExaminationFormID(entity.getId());
        kafkaTemplate.send(CREATE_PATIENT_TOPIC, request.getPatient());
    }

    public List<ExaminationFormResponse> findReceivedPatientsToday(){
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0);
        LocalDateTime end = start.plusDays(1);
        return examinationFormMapperUtil.entitiesToResponses(examinationFormRepository.findByCreatedAtIsBetweenOrderByCreatedAtDesc(start, end));
    }

    @Transactional
    public ExaminationFormResponse saveWithPatientInformation(CreateForWithPatientInforRequest request){
        ExaminationForm entity = examinationFormMapperUtil.createRequestWithPatientInformationToEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setEmployeeId(auditingUtil.getUserLogged().getId());
        entity = examinationFormRepository.save(entity);

        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, examinationFormMapperUtil.buildCreateExaminationResultRequest(entity));
        return examinationFormMapperUtil.mapEntityToResponse(entity);
    }

    @Transactional
    public ExaminationFormResponse saveWithAppointment(CreateFormWithAppointmentRequest request){
        ExaminationForm entity = examinationFormMapperUtil.createRequestWithAppointmentToEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setEmployeeId(auditingUtil.getUserLogged().getId());
        entity = examinationFormRepository.save(entity);

        CreateExaminationResultCommonRequest createExaminationResultRequest =  examinationFormMapperUtil.buildCreateExaminationResultRequest(entity);
        createExaminationResultRequest.setSymptom(request.getSymptom());
        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, createExaminationResultRequest);
        return examinationFormMapperUtil.mapEntityToResponse(entity);
    }

    @KafkaListener(topics = CREATED_PATIENT_INFORMATION_TOPIC, groupId = GREETING_GROUP)
    public void handleCreatedPatientInformation(UUID examinationFormId){
        log.info(String.format("Received request created Patient information from kafka with id: %s", examinationFormId));
        ExaminationForm form = examinationFormRepository.findById(examinationFormId).orElseThrow(() -> new NotFoundException(GREETING_EXCEPTION.FORM_NOT_FOUND));
        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, examinationFormMapperUtil.buildCreateExaminationResultRequest(form));
    }

    @KafkaListener(topics = UPDATE_NUMBER_EXAMINATION_FORM_TOPIC, groupId = GREETING_GROUP)
    public void handleUpdateNumberExaminationFormTopic(UpdateNumberExaminationFormRequest request){
        log.info("Received request update number for examination form from kafka for id: " + request.getId());
        ExaminationForm form = examinationFormRepository.findById(request.getId()).orElseThrow(() -> BaseException.buildNotFound().message(GREETING_EXCEPTION.FORM_NOT_FOUND).build());
        form.setExaminedNumber(request.getExaminedNumber());
        examinationFormRepository.save(form);

        ExaminationFormResponse updatedResponse = examinationFormMapperUtil.mapEntityToResponse(form);
        kafkaTemplate.send(UPDATED_NUMBER_EXAMINATION_FORM_TOPIC, updatedResponse);
    }
}