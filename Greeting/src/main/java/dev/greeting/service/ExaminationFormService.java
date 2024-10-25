package dev.greeting.service;

import static dev.common.constant.KafkaTopicsConstrant.*;

import com.google.gson.Gson;
import dev.common.constant.ExceptionConstant.GREETING_EXCEPTION;
import dev.common.dto.response.appointment.ExaminationFormResponse;
import dev.common.exception.NotFoundException;
import dev.common.model.AuthenticatedUser;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationFormService {
    private final ExaminationFormRepository examinationFormRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ExaminationFormMapperUtil examinationFormMapperUtil;
    private final Gson gson;

    @Value(CREATE_PATIENT_ACCOUNT_FROM_GREETING_TOPIC)
    private String CREATE_PATIENT_TOPIC;

    @Value(CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC)
    private String CREATE_EXAMINATION_RESULT_TOPIC;

    @Transactional
    public ExaminationFormResponse saveFirstTimePatient(CreateFormForFirstTimePatientRequest request){
        ExaminationForm entity = examinationFormMapperUtil.createRequestToEntity(request);
        request.getPatient().setExaminationFormID(entity.getId());
        AuthenticatedUser employee = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        entity.setEmployeeId(employee.getId());
        entity.setCreatedAt(LocalDateTime.now());
        entity = examinationFormRepository.save(entity);

        kafkaTemplate.send(CREATE_PATIENT_TOPIC, request.getPatient());
        //Todo: Thêm bắn thông tin tới websocket
        return examinationFormMapperUtil.entityToResponse(entity);
    }

    @Transactional
    public ExaminationFormResponse saveWithPatientInformation(CreateForWithPatientInforRequest request){
        ExaminationForm entity = examinationFormMapperUtil.createRequestToEntity(request);
        entity = examinationFormRepository.save(entity);

        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, examinationFormMapperUtil.buildCreateExaminationResultRequest(entity));
        //Todo: Thêm bắn thông tin tới websocket
        return examinationFormMapperUtil.entityToResponse(entity);
    }

    @KafkaListener(topics = CREATED_PATIENT_INFORMATION_TOPIC, groupId = GREETING_GROUP)
    public void handleCreatedPatientInformation(UUID examinationFormId){
        log.info(String.format("Received request created Patient information from kafka with id: %s", examinationFormId));
        ExaminationForm form = examinationFormRepository.findById(examinationFormId).orElseThrow(() -> new NotFoundException(GREETING_EXCEPTION.FORM_NOT_FOUND));
        kafkaTemplate.send(CREATE_EXAMINATION_RESULT_TOPIC, examinationFormMapperUtil.buildCreateExaminationResultRequest(form));
    }
}