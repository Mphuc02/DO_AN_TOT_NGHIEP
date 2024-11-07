package dev.patient.util;

import dev.common.dto.response.patient.AppointmentImageDetailResponse;
import dev.patient.entity.AppointmentImageDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentImageDetailMapperUtil {
    List<AppointmentImageDetailResponse> mapEntitiesToResponses(List<AppointmentImageDetail> entites);
}