package dev.patient.service;

import com.google.gson.Gson;
import dev.common.dto.request.CreateNewPatientRequest;
import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.patient.dto.response.PatientResponse;
import dev.patient.entity.Patient;
import dev.patient.repository.PatientRepository;
import dev.patient.util.AddressMapperUtil;
import dev.patient.util.FullNameMapperUtil;
import dev.patient.util.PatientMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapperUtil patientUtil;
    private final Gson gson;
    private final AddressMapperUtil addressMapperUtil;
    private final FullNameMapperUtil fullNameMapperUtil;

    public List<PatientResponse> getAll(){
        return patientUtil.mapEntitiesToResponses(patientRepository.findAll());
    }

    public boolean checkPatientExist(UUID id){
        return patientRepository.existsById(id);
    }

    @KafkaListener(topics = CREATE_PATIENT_FROM_GREETING_TOPIC, groupId = PATIENT_GROUP)
    public void createPatientFromGreeting(CreateNewPatientRequest request){
        log.info(String.format("Receive request create patient from kafka: %s", gson.toJson(request)));

        Patient patient = patientUtil.createRequestToEntity(request);
        patient.setAddress(addressMapperUtil.createRequestToEntity(request.getAddress()));
        patient.getAddress().setPatient(patient);

        patient.setFullName(fullNameMapperUtil.createRequestToEntity(request.getFullName()));
        patient.getFullName().setPatient(patient);

        patientRepository.save(patient);
    }
}