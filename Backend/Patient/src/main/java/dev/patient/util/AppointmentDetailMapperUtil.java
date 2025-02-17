package dev.patient.util;

import dev.common.dto.response.patient.AppointmentDetailResponse;
import dev.patient.entity.AppointmentDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentDetailMapperUtil {
    List<AppointmentDetailResponse> mapEntitiesToResponses(List<AppointmentDetail> entities);
}