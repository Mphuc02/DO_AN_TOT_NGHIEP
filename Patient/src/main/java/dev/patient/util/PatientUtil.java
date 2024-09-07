package dev.patient.util;

import dev.common.dto.request.CreateNewPatientRequest;
import dev.patient.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientUtil {
    private final FullNameUtil fullNameUtil;
    private final AddressUtil addressUtil;

    public Patient createRequestToEntity(CreateNewPatientRequest request){
        Patient patient = Patient.builder()
                .id(request.getId())
                .usingAccount(false)
                .build();

        patient.setFullName(fullNameUtil.createRequestToEntity(request.getFullName(), patient));
        patient.setAddress(addressUtil.createRequestToEntity(request.getAddress(), patient));
        return patient;
    }
}