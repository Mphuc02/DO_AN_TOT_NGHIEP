package dev.patient.util;

import dev.common.dto.request.CreateWithFullNameCommonRequest;
import dev.common.dto.request.UpdateWithFullNameRequest;
import dev.common.dto.response.user.FullNameCommonResponse;
import dev.common.dto.response.patient.FullNameResponse;
import dev.patient.entity.FullName;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FullNameMapperUtil {
    @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "reFormatName")
    @Mapping(target = "middleName", source = "request.middleName", qualifiedByName = "reFormatName")
    @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "reFormatName")
    FullName createRequestToEntity(CreateWithFullNameCommonRequest request);


    @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "reFormatName")
    @Mapping(target = "middleName", source = "request.middleName", qualifiedByName = "reFormatName")
    @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "reFormatName")
    void updateRequestToEntity(UpdateWithFullNameRequest request, @MappingTarget FullName entity);

    FullNameCommonResponse entityToCommonResponse(FullName fullName);
    FullNameResponse entityToResponse(FullName fullName);

    @Named("reFormatName")
    default String reFormatName(String name){
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}