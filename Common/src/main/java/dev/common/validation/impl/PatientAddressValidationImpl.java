package dev.common.validation.impl;

import dev.common.client.AddressClient;
import dev.common.dto.request.CheckAddressRequest;
import dev.common.dto.request.CreatePatientAddressRequest;
import dev.common.validation.PatientAddressValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class PatientAddressValidationImpl implements ConstraintValidator<PatientAddressValidation, CreatePatientAddressRequest> {
    private final AddressClient client;

    @Override
    public boolean isValid(CreatePatientAddressRequest createPatientAddressRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(createPatientAddressRequest))
            return true;

        CheckAddressRequest request = CheckAddressRequest.builder()
                .provinceId(createPatientAddressRequest.getProvinceId())
                .districtId(createPatientAddressRequest.getDistrictId())
                .communeId(createPatientAddressRequest.getCommuneId())
                .build();

        return client.checkAddress(request);
    }
}