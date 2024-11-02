package dev.patient.service;

import dev.common.constant.ExceptionConstant.*;
import static dev.common.constant.KafkaTopicsConstrant.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapperUtil appointmentMapperUtil;
    private final AuditingUtil auditingUtil;

    public List<AppointmentResponse> getAppointmentsOfToday(){
        return appointmentMapperUtil.mapEntitiesToResponses(appointmentRepository.findByAppointmentDateAndIsExamined(LocalDate.now(), false));
    }

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request){
        if(appointmentRepository.existsByPatientIdAndAppointmentDate(auditingUtil.getUserLogged().getId(), request.getAppointmentDate())){
            ErrorField error = new ErrorField(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_DATE, CreateAppointmentRequest.Fields.appointmentDate);
            throw BaseException.buildBadRequest().addField(error).build();
        }

        Appointment appointment = appointmentMapperUtil.mapCreateRequestToEntity(request);
        if(request.getDiseasesIds() != null){
            List<AppointmentDetail> details = new ArrayList<>();
            for(UUID id: request.getDiseasesIds()){
                AppointmentDetail detail = AppointmentDetail.builder().appointment(appointment).diseaseId(id).build();
                details.add(detail);
            }
            appointment.setDetails(details);
        }

        if(request.getImages() != null){
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

    @Transactional
    @KafkaListener(topics = APPOINTMENT_HAD_BEEN_EXAMINED_TOPIC, groupId = PATIENT_GROUP)
    public void updateIsExaminedAppointment(UUID appointmentId){
        log.info("Received request update isExamined for Appointment from kafka with id: " + appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> BaseException.buildNotFound().message(PATIENT_EXCEPTION.APPOINTMENT_NOT_FOUND).build());
        appointment.setIsExamined(true);
        appointmentRepository.save(appointment);
    }
}