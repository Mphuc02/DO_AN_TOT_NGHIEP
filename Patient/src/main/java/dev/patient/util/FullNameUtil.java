package dev.patient.util;

import dev.common.dto.request.CreateFullNameRequest;
import dev.common.dto.request.UpdateFullNameRequest;
import dev.common.dto.response.FullNameResponse;
import dev.patient.entity.FullName;
import dev.patient.entity.Patient;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class FullNameUtil {
    public FullName createRequestToEntity(CreateFullNameRequest request, Patient patient){
        return FullName.builder()
                .firstName(reFormatName(request.getFirstName()))
                .middleName(reFormatName(request.getMiddleName()))
                .lastName(reFormatName(request.getLastName()))
                .patient(patient)
                .build();
    }

    public FullNameResponse entityToResponse(FullName entity){
        return FullNameResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .build();
    }

    public void updateRequestToEntity(UpdateFullNameRequest request, FullName entity){
        if(!ObjectUtils.isEmpty(request.getFirstName()))
            entity.setFirstName(reFormatName(request.getFirstName()));

        if(!ObjectUtils.isEmpty(request.getMiddleName()))
            entity.setMiddleName(reFormatName(request.getMiddleName()));

        if(!ObjectUtils.isEmpty(request.getLastName()))
            entity.setLastName((reFormatName(request.getLastName())));
    }

    private String reFormatName(String name){
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}