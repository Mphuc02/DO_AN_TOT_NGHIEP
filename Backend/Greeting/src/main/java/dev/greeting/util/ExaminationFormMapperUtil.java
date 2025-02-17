package dev.greeting.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.CreateNewPatientCommonRequest;
import dev.common.dto.response.examination_form.ExaminationFormResponse;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
import dev.greeting.dto.request.CreateFormWithAppointmentRequest;
import dev.greeting.entity.ExaminationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationFormMapperUtil {
    ExaminationForm createRequestWithPatientInformationToEntity(CreateForWithPatientInforRequest request);

    @Mapping(target = "patientId", source = "request.patient", qualifiedByName = "mapPatientId")
    ExaminationForm createRequestWithFirstTimePatientToEntity(CreateFormForFirstTimePatientRequest request);

    ExaminationForm createRequestWithAppointmentToEntity(CreateFormWithAppointmentRequest request);

    ExaminationFormResponse mapEntityToResponse(ExaminationForm entity);
    List<ExaminationFormResponse> entitiesToResponses(List<ExaminationForm> entities);

    CreateExaminationResultCommonRequest buildCreateExaminationResultRequest(ExaminationForm form);

    @Named(value = "mapPatientId")
    default UUID mapPatientId(CreateNewPatientCommonRequest request){
        return request.getId();
    }
}