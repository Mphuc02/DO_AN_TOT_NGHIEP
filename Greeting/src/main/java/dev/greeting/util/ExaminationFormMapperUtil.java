package dev.greeting.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.CreateNewPatientRequest;
import dev.common.dto.response.ExaminationFormResponse;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
import dev.greeting.entity.ExaminationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationFormMapperUtil {
    ExaminationForm createRequestToEntity(CreateForWithPatientInforRequest request);

    @Mapping(target = "patientId", source = "request.patient", qualifiedByName = "mapPatientId")
    ExaminationForm createRequestToEntity(CreateFormForFirstTimePatientRequest request);

    ExaminationFormResponse entityToResponse(ExaminationForm entity);

    CreateExaminationResultCommonRequest buildCreateExaminationResultRequest(ExaminationForm form);

    @Named(value = "mapPatientId")
    default UUID mapPatientId(CreateNewPatientRequest request){
        return request.getId();
    }
}