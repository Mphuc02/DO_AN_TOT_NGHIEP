package dev.examinationresult.util;

import dev.examinationresult.dto.request.SaveMedicineConsultationFormDetailRequest;
import dev.examinationresult.entity.MedicineConsultationFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicineConsultationFormDetailMapperUtil {
    MedicineConsultationFormDetail mapSaveRequestToEntity(SaveMedicineConsultationFormDetailRequest request);
}