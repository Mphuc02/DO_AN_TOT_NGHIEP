package dev.patient.util;

import dev.common.dto.request.CreateNewPatientCommonRequest;
import dev.common.dto.response.patient.PatientResponse;
import dev.patient.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientMapperUtil {
    Patient createRequestToEntity(CreateNewPatientCommonRequest request);
    PatientResponse mapEntityToResponse(Patient patient);
    List<PatientResponse> mapEntitiesToResponses(List<Patient> patients);
}