package dev.common.validator.impl;

import dev.common.client.AddressClient;
import dev.common.dto.request.CheckAddressRequest;
import dev.common.dto.request.CreatePatientAddressRequest;
import dev.common.validator.PatientAddressValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class PatientAddressValidatorImpl implements ConstraintValidator<PatientAddressValidator, CreatePatientAddressRequest> {
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