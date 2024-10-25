package dev.examinationresult.util;

import dev.examinationresult.dto.request.CreateAppointmentFormRequest;
import dev.common.dto.response.examination_result.AppointmentFormResponse;
import dev.examinationresult.entity.AppointmentForm;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentFormMapperUtil {
    AppointmentForm mapCreateRequestToEntity(CreateAppointmentFormRequest request);
    AppointmentFormResponse mapEntityToResponse(AppointmentForm form);
}