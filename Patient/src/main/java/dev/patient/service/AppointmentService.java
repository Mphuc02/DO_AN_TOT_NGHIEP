package dev.patient.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.BaseException;
import dev.common.exception.NotFoundException;
import dev.common.model.ErrorField;
import dev.common.util.AuditingUtil;
import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.common.dto.response.patient.AppointmentResponse;
import dev.patient.entity.Appointment;
import dev.patient.entity.AppointmentDetail;
import dev.patient.entity.AppointmentImageDetail;
import dev.patient.repository.AppointmentRepository;
import dev.patient.util.AppointmentMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapperUtil appointmentMapperUtil;
    private final AuditingUtil auditingUtil;

    public List<AppointmentResponse> getAppointmentsOfToday(){
        return appointmentMapperUtil.mapEntitiesToResponses(appointmentRepository.findByAppointmentDate(LocalDate.now()));
    }

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request){
        if(appointmentRepository.existsByPatientIdAndAppointmentDate(auditingUtil.getUserLogged().getId(), request.getAppointmentDate())){
            ErrorField error = new ErrorField(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_DATE, CreateAppointmentRequest.Fields.appointmentDate);
            throw BaseException.buildBadRequest().addField(error).build();
        }

        Appointment appointment = appointmentMapperUtil.mapCreateRequestToEntity(request);
        if(appointment.getDetails() != null){
            for (AppointmentDetail detail : appointment.getDetails()) {
                detail.setAppointment(appointment);
            }
        }

        if(appointment.getImages() != null){
            for (AppointmentImageDetail image : appointment.getImages()) {
                image.setAppointment(appointment);
            }
        }

        appointment.setPatientId(auditingUtil.getUserLogged().getId());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment = appointmentRepository.save(appointment);
        return appointmentMapperUtil.mapEntityToResponse(appointment);
    }

    @Transactional
    public AppointmentResponse update(UUID id, UpdateAppointmentRequest request){
        if(appointmentRepository.existsByPatientIdAndAppointmentDate(auditingUtil.getUserLogged().getId(), request.getAppointmentDate())){
            ErrorField error = new ErrorField(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_DATE, CreateAppointmentRequest.Fields.appointmentDate);
            throw BaseException.buildBadRequest().addField(error).build();
        }

        Appointment appointment = checkPermission(id);
        appointmentMapperUtil.mapUpdateRequestToEntity(request, appointment);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapperUtil.mapEntityToResponse(appointment);
    }

    @Transactional
    public void delete(UUID id){
        Appointment appointment = checkPermission(id);
        appointmentRepository.delete(appointment);
    }

    private Appointment checkPermission(UUID id){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException(PATIENT_EXCEPTION.APPOINTMENT_NOT_FOUND));
        if(!Objects.equals(appointment.getPatientId(), auditingUtil.getUserLogged().getId())){
            throw BaseException.buildNotFound().message(PATIENT_EXCEPTION.NOT_PERMISSION_WITH_APPOINTMENT).build();
        }

        LocalDate today = LocalDate.now();
        if(appointment.getAppointmentDate().isBefore(today)){
            throw BaseException.buildNotFound().message(PATIENT_EXCEPTION.CAN_NOT_UPDATE_PASSED_APPOINTMENT).build();
        }
        return appointment;
    }
}