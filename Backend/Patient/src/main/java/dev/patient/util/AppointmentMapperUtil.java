package dev.patient.util;

import dev.common.dto.response.patient.PatientResponse;
import dev.patient.dto.request.CreateAppointmentByDoctorRequest;
import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.common.dto.response.patient.AppointmentResponse;
import dev.patient.entity.Appointment;
import dev.patient.entity.Patient;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapperUtil {
    Appointment mapCreateRequestToEntity(CreateAppointmentRequest request);
    Appointment mapCreateRequestToEntity(CreateAppointmentByDoctorRequest request);

    @Mapping(target = "patientId", source = "appointment.patient", qualifiedByName = "mapPatientId")
    AppointmentResponse mapEntityToResponse(Appointment appointment);
    void mapUpdateRequestToEntity(UpdateAppointmentRequest request, @MappingTarget Appointment appointment);
    List<AppointmentResponse> mapEntitiesToResponses(List<Appointment> appointments);

    @Named("mapPatientId")
    default UUID mapPatientId(Patient patient){
        return patient.getId();
    }
}