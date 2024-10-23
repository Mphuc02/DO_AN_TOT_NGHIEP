package dev.patient.util;

import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.patient.dto.response.AppointmentResponse;
import dev.patient.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapperUtil {
    Appointment mapCreateRequestToEntity(CreateAppointmentRequest request);
    AppointmentResponse mapEntityToResponse(Appointment appointment);
    void mapUpdateRequestToEntity(UpdateAppointmentRequest request, @MappingTarget Appointment appointment);
    List<AppointmentResponse> mapEntitiesToResponses(List<Appointment> appointments);
}