package dev.examinationresult.util;

import dev.examinationresult.dto.request.SaveMedicineConsultationFormRequest;
import dev.common.dto.response.examination_result.MedicineConsultationFormResponse;
import dev.examinationresult.entity.MedicineConsultationForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicineConsultationFormMapperUtil {
    MedicineConsultationForm mapCreateRequestToEntity(SaveMedicineConsultationFormRequest request);
    void mapUpdateRequestToEntity(SaveMedicineConsultationFormRequest request, @MappingTarget MedicineConsultationForm form);
    MedicineConsultationFormResponse mapEntityToResponse(MedicineConsultationForm form);
    List<MedicineConsultationFormResponse> mapEntitiesToResponses(List<MedicineConsultationForm> forms);
}