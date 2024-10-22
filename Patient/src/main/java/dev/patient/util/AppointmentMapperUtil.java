package dev.patient.util;

import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.patient.dto.response.AppointmentResponseDTO;
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
    AppointmentResponseDTO mapEntityToResponse(Appointment appointment);
    void mapUpdateRequestToEntity(UpdateAppointmentRequest request, @MappingTarget Appointment appointment);
    List<AppointmentResponseDTO> mapEntitiesToResponses(List<Appointment> appointments);
}