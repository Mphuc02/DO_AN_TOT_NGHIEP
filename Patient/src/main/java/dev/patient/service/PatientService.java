package dev.patient.service;

import com.google.gson.Gson;
import dev.common.dto.request.CreateNewPatientRequest;
import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.patient.entity.Patient;
import dev.patient.repository.PatientRepository;
import dev.patient.util.PatientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientUtil patientUtil;
    private final Gson gson;

    @KafkaListener(topics = CREATE_PATIENT_FROM_GREETING_TOPIC, groupId = PATIENT_GROUP)
    public void createPatientFromGreeting(CreateNewPatientRequest request){
        log.info(String.format("Receive request create patient from kafka: %s", gson.toJson(request)));
        Patient patient = patientUtil.createRequestToEntity(request);
        patientRepository.save(patient);
    }
}