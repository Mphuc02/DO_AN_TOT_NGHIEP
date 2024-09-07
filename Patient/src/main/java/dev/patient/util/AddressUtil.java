package dev.patient.util;

import dev.common.dto.request.CreatePatientAddressRequest;
import dev.patient.entity.Address;
import dev.patient.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class AddressUtil {
    public Address createRequestToEntity(CreatePatientAddressRequest request, Patient patient){
        return Address.builder()
                .street(request.getStreet())
                .provinceId(request.getProvinceId())
                .districtId(request.getDistrictId())
                .communeId(request.getCommuneId())
                .patient(patient)
                .build();
    }
}