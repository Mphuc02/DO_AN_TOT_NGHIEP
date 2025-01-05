package dev.patient.service;

import com.google.gson.Gson;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateNewPatientCommonRequest;
import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.dto.response.patient.PatientResponse;
import dev.common.exception.BaseException;
import dev.common.util.AuditingUtil;
import dev.patient.entity.Patient;
import dev.patient.repository.PatientRepository;
import dev.patient.util.AddressMapperUtil;
import dev.patient.util.FullNameMapperUtil;
import dev.patient.util.PatientMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapperUtil patientMapperUtil;
    private final Gson gson;
    private final AddressMapperUtil addressMapperUtil;
    private final FullNameMapperUtil fullNameMapperUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuditingUtil auditingUtil;


    @Value(KafkaTopicsConstrant.CREATED_PATIENT_INFORMATION_TOPIC)
    private String CREATED_PATIENT_INFORMATION_TOPIC;

    public List<PatientResponse> getAll(){
        return patientMapperUtil.mapEntitiesToResponses(patientRepository.findAll());
    }

    public PatientResponse findById(UUID id){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> BaseException.buildNotFound().message(PATIENT_EXCEPTION.PATIENT_NOT_FOUND).build());
        return patientMapperUtil.mapEntityToResponse(patient);
    }

    public List<PatientResponse> getByIds(List<UUID> ids){
        return patientMapperUtil.mapEntitiesToResponses(patientRepository.findAllById(ids));
    }

    public boolean checkPatientExist(UUID id){
        return patientRepository.existsById(id);
    }

    public PatientResponse getLoggedUserInformation(){
        UUID userId = auditingUtil.getUserLogged().getId();
        return findById(userId);
    }

    @KafkaListener(topics = CREATED_PATIENT_ACCOUNT_SUCCESS_TOPIC, groupId = PATIENT_GROUP)
    public void createPatientFromGreeting(CreateNewPatientCommonRequest request){
        log.info("Receive request create patient from kafka:");

        Patient patient = patientMapperUtil.createRequestToEntity(request);
        patient.setAddress(addressMapperUtil.createRequestToEntity(request.getAddress()));
        patient.getAddress().setPatient(patient);

        patient.setFullName(fullNameMapperUtil.createRequestToEntity(request.getFullName()));
        patient.getFullName().setPatient(patient);

        patientRepository.save(patient);
        kafkaTemplate.send(CREATED_PATIENT_INFORMATION_TOPIC, request.getExaminationFormID());
    }
}