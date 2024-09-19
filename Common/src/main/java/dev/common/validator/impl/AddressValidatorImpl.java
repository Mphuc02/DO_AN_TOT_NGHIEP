package dev.common.validator.impl;

import dev.common.client.AddressClient;
import dev.common.dto.request.CheckAddressRequest;
import dev.common.dto.request.CreatePatientAddressRequest;
import dev.common.validator.AddressValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class AddressValidatorImpl implements ConstraintValidator<AddressValidator, CreatePatientAddressRequest> {
    private final AddressClient client;

    @Override
    public boolean isValid(CreatePatientAddressRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(request))
            return true;

        if(ObjectUtils.isEmpty(request.getProvinceId()) ||
                ObjectUtils.isEmpty(request.getCommuneId()) ||
                ObjectUtils.isEmpty(request.getDistrictId())){

            request.setCommuneId(null);
            request.setProvinceId(null);
            request.setDistrictId(null);
            request.setStreet(null);
            return true;
        }

        CheckAddressRequest checkRequest = CheckAddressRequest.builder()
                                                .provinceId(request.getProvinceId())
                                                .districtId(request.getDistrictId())
                                                .communeId(request.getCommuneId())
                                                .build();

        return client.checkAddress(checkRequest);
    }
}