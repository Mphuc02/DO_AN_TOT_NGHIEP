package dev.patient.util;

import dev.common.dto.request.CreateFullNameRequest;
import dev.common.dto.request.UpdateFullNameRequest;
import dev.common.dto.response.FullNameCommonResponse;
import dev.patient.dto.response.FullNameResponse;
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
    FullName createRequestToEntity(CreateFullNameRequest request);


    @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "reFormatName")
    @Mapping(target = "middleName", source = "request.middleName", qualifiedByName = "reFormatName")
    @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "reFormatName")
    void updateRequestToEntity(UpdateFullNameRequest request, @MappingTarget FullName entity);

    FullNameCommonResponse entityToCommonResponse(FullName fullName);
    FullNameResponse entityToResponse(FullName fullName);

    @Named("reFormatName")
    default String reFormatName(String name){
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}