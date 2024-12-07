package dev.employee.util;

import dev.common.dto.request.CreateWithFullNameCommonRequest;
import dev.common.dto.request.UpdateWithFullNameRequest;
import dev.common.dto.response.user.FullNameCommonResponse;
import dev.employee.entity.Employee;
import dev.employee.entity.FullName;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class FullNameUtil {
    public FullName createRequestToEntity(CreateWithFullNameCommonRequest request, Employee employee){
        return FullName.builder()
                .firstName(reFormatName(request.getFirstName()))
                .middleName(reFormatName(request.getMiddleName()))
                .lastName(reFormatName(request.getLastName()))
                .employee(employee)
                .build();
    }

    public FullNameCommonResponse entityToResponse(FullName entity){
        return FullNameCommonResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .build();
    }

    public void updateRequestToEntity(UpdateWithFullNameRequest request, FullName entity){
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