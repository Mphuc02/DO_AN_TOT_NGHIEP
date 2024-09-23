package dev.medicine.validator.impl;

import dev.common.client.AddressClient;
import dev.common.dto.request.CheckAddressRequest;
import dev.medicine.dto.request.save.SaveAddressRequest;
import dev.medicine.validator.AddressValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class AddressValidatorImpl implements ConstraintValidator<AddressValidator, SaveAddressRequest> {
    private final AddressClient client;

    @Override
    public boolean isValid(SaveAddressRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(request))
            return true;

        if(ObjectUtils.isEmpty(request.getProvinceId()) ||
        ObjectUtils.isEmpty(request.getDistrictId()) ||
        ObjectUtils.isEmpty(request.getCommuneId()))
            return true;

        CheckAddressRequest checkRequest = CheckAddressRequest.builder()
                                                .provinceId(request.getProvinceId())
                                                .districtId(request.getDistrictId())
                                                .communeId(request.getCommuneId())
                                                .build();

        return client.checkAddress(checkRequest);
    }
}