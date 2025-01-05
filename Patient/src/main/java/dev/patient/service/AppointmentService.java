package dev.patient.service;

import dev.common.constant.ExceptionConstant.*;
import static dev.common.constant.KafkaTopicsConstrant.*;

import dev.common.dto.request.GetOrderNumberAppointmentRequest;
import dev.common.dto.response.patient.AppointmentImageDetailResponse;
import dev.common.exception.BaseException;
import dev.common.exception.NotFoundException;
import dev.common.model.ErrorField;
import dev.common.util.AuditingUtil;
import dev.patient.dto.request.CreateAppointmentByDoctorRequest;
import dev.patient.dto.request.CreateAppointmentRequest;
import dev.patient.dto.request.UpdateAppointmentRequest;
import dev.common.dto.response.patient.AppointmentResponse;
import dev.patient.entity.Appointment;
import dev.patient.entity.AppointmentDetail;
import dev.patient.entity.AppointmentImageDetail;
import dev.patient.entity.Patient;
import dev.patient.repository.AppointmentDetailRepository;
import dev.patient.repository.AppointmentImageDetailRepository;
import dev.patient.repository.AppointmentRepository;
import dev.patient.util.AppointmentDetailMapperUtil;
import dev.patient.util.AppointmentImageDetailMapperUtil;
import dev.patient.util.AppointmentMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final AppointmentMapperUtil appointmentMapperUtil;
    private final AppointmentDetailMapperUtil appointmentDetailMapperUtil;
    private final AppointmentImageDetailRepository appointmentImageDetailRepository;
    private final AppointmentImageDetailMapperUtil appointmentImageDetailMapperUtil;
    private final AuditingUtil auditingUtil;

    public List<AppointmentResponse> getAppointmentByTime(int year, int month){
        UUID patientId = auditingUtil.getUserLogged().getId();
        LocalDate thisMonth = LocalDate.of(year, month, 1);
        LocalDate nextMonth = thisMonth.plusMonths(1);
        return appointmentMapperUtil.mapEntitiesToResponses(appointmentRepository.findByAppointmentDateBetweenAndPatientIdOrderByAppointmentDate(thisMonth, nextMonth, patientId));
    }

    public List<AppointmentResponse> getAppointmentsOfToday(){
        return appointmentMapperUtil.mapEntitiesToResponses(appointmentRepository.findByAppointmentDateAndIsExaminedOrderByCreatedAt(LocalDate.now(), false));
    }

    public List<AppointmentImageDetailResponse> findDetailsByAppointmentId(UUID id){
        return appointmentImageDetailMapperUtil.mapEntitiesToResponses(appointmentImageDetailRepository.findByAppointmentId(id));
    }

    @Transactional
    public AppointmentResponse create(CreateAppointmentByDoctorRequest request){
        if(appointmentRepository.existsByExaminationResultId(request.getExaminationResultId())){
            ErrorField error = new ErrorField(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_FOR_EXAMINATION_RESULT, CreateAppointmentByDoctorRequest.Fields.examinationResultId);
            throw BaseException.buildBadRequest().message(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_FOR_EXAMINATION_RESULT).addField(error).build();
        }

        if(appointmentRepository.existsByPatientIdAndAppointmentDate(auditingUtil.getUserLogged().getId(), request.getAppointmentDate())){
            ErrorField error = new ErrorField(PATIENT_EXCEPTION.DUPLICATE_APPOINTMENT_DATE, CreateAppointmentRequest.Fields.appointmentDate);
            throw BaseException.buildBadRequest().addField(error).build();
        }

        Appointment appointment = appointmentMapperUtil.mapCreateRequestToEntity(request);
        appointment.setDoctorId(auditingUtil.getUserLogged().getId());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setIsExamined(false);
        appointment.setPatient(new Patient(request.getPatientId()));
        appointment = appointmentRepository.save(appointment);
        return appointmentMapperUtil.mapEntityToResponse(appointment);
    }

    @Transactional
    public AppointmentResponse  create(CreateAppointmentRequest request){
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

        appointment.setPatient(new Patient(auditingUtil.getUserLogged().getId()));
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setIsExamined(false);
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
        if(!Objects.equals(appointment.getPatient().getId(), auditingUtil.getUserLogged().getId())){
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

    public int countNumberAppointmentTodayOfDoctor(UUID doctorId){
        LocalDate today = LocalDate.now();
        return appointmentRepository.countAppointmentTodayOfDoctor(doctorId, today);
    }

    public int getOrderOfAppointment(GetOrderNumberAppointmentRequest request){
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(() -> BaseException.buildNotFound().message(PATIENT_EXCEPTION.APPOINTMENT_NOT_FOUND).build());
        LocalDate today = LocalDate.now();
        return appointmentRepository.getOrderNumberOfAppointment(request.getDoctorId(), request.getAppointmentId(), today, appointment.getCreatedAt());
    }

    public AppointmentResponse findById(UUID id){
        Appointment appointment = appointmentRepository.findByExaminationResultId(id);
        return appointment == null ? null : appointmentMapperUtil.mapEntityToResponse(appointment);
    }
}