package dev.examinationresult.service;

import static dev.common.constant.KafkaTopicsConstrant.*;

import dev.common.client.AppointmentClient;
import dev.common.client.WorkingScheduleClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.*;
import dev.common.dto.response.working_schedule.WorkingScheduleResponse;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.model.AuthenticatedUser;
import dev.common.util.AuditingUtil;
import dev.examinationresult.dto.request.UpdateExaminationResultRequest;
import dev.common.dto.response.examination_result.ExaminationResultResponse;
import dev.examinationresult.entity.ExaminationResult;
import dev.examinationresult.entity.ExaminationResultDetail;
import dev.examinationresult.repository.ExaminationResultRepository;
import dev.examinationresult.util.ExaminationResultDetailMapperUtil;
import dev.examinationresult.util.ExaminationResultMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationResultService {
    private final ExaminationResultMapperUtil resultMapperUtil;
    private final ExaminationResultRepository examinationResultRepository;
    private final ExaminationResultDetailMapperUtil resultDetailMapperUtil;
    private final WorkingScheduleClient workingScheduleClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuditingUtil auditingUtil;
    private final AppointmentClient appointmentClient;

    @Value(KafkaTopicsConstrant.CREATED_EXAMINATION_RESULT_SUCCESS_TOPIC)
    private String CREATED_EXAMINATION_RESULT_SUCCESS;

    @Value(KafkaTopicsConstrant.APPOINTMENT_HAD_BEEN_EXAMINED_TOPIC)
    private String APPOINTMENT_HAD_BEEN_EXAMINED;

    @Value(KafkaTopicsConstrant.UPDATE_NUMBER_EXAMINATION_FORM_TOPIC)
    private String UPDATE_NUMBER_EXAMINATION_FORM_TOPIC;

    @Value(KafkaTopicsConstrant.CREATE_RELATION_SHIP_TOPIC)
    private String CREATE_RELATION_SHIP_TOPIC;

    public ExaminationResultResponse getById(UUID id){
        ExaminationResult findById = examinationResultRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(EXAMINATION_RESULT_EXCEPTION.RESULT_NOT_FOUND));
        return resultMapperUtil.mapEntityToResponse(findById);
    }

    public List<ExaminationResultResponse> findWaitingExaminationPatients(){
        UUID doctorId = auditingUtil.getUserLogged().getId();
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0);
        LocalDateTime end = start.plusDays(1);
        return resultMapperUtil.mapEntitiesToResponses(examinationResultRepository.findWaitingExaminationPatientsOfDoctor(doctorId, start, end));
    }

    @KafkaListener(topics = CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC,
                    groupId = EXAMINATION_RESULT_GROUP)
    public void create(CreateExaminationResultCommonRequest request){
        log.info("receive request create examination result from kafka");
        ExaminationResult result = resultMapperUtil.mapCreateRequestToEntity(request);
        result.setCreatedAt(LocalDateTime.now());
        WorkingScheduleResponse schedule = workingScheduleClient.getById(request.getWorkingScheduleId());
        result.setEmployeeId(schedule.getEmployeeId());

        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0);
        LocalDateTime end = start.plusDays(1);

        if(request.getAppointmentId() != null){
            GetOrderNumberAppointmentRequest getNumberRequest = new GetOrderNumberAppointmentRequest(request.getAppointmentId(), schedule.getEmployeeId());
            int number = appointmentClient.getOrderNumberOfAppointment(getNumberRequest);
            result.setExaminedNumber(number);
        }else{
            ExaminationResult lastExaminationResult = examinationResultRepository.getLastResultOfDoctor(result.getEmployeeId(), start, end);
            int numberAppointments = appointmentClient.countAppointmentTodayOfDoctor(result.getEmployeeId());
            if(lastExaminationResult == null){
                result.setExaminedNumber(numberAppointments + 1);
            }else{
                if(lastExaminationResult.getExaminedNumber() < numberAppointments){
                    result.setExaminedNumber(numberAppointments + 1);
                }else{
                    result.setExaminedNumber(lastExaminationResult.getExaminedNumber() + 1);
                }
            }
        }

        result = examinationResultRepository.save(result);

        CreateInvoiceCommonRequest createInvoiceRequest = resultMapperUtil.mapEntityToCreateInvoiceRequest(result);
        kafkaTemplate.send(CREATED_EXAMINATION_RESULT_SUCCESS, createInvoiceRequest);

        //If request has appointment, update isExamined of appointment
        if(request.getAppointmentId() != null){
            kafkaTemplate.send(APPOINTMENT_HAD_BEEN_EXAMINED, request.getAppointmentId());
        }

        //Update number in Examination form
        UpdateNumberExaminationFormRequest updateExaminationFormRequest = UpdateNumberExaminationFormRequest.builder()
                                                                            .id(request.getId())
                                                                            .examinedNumber(result.getExaminedNumber())
                                                                            .build();
        kafkaTemplate.send(UPDATE_NUMBER_EXAMINATION_FORM_TOPIC, updateExaminationFormRequest);
    }

    @Transactional
    public ExaminationResultResponse update(UpdateExaminationResultRequest request, UUID id){
        ExaminationResult findToUpdate = examinationResultRepository.findById(id)
                                                        .orElseThrow(() ->
                                                                new NotFoundException(EXAMINATION_RESULT_EXCEPTION.RESULT_NOT_FOUND));

        UUID employeeId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if(!employeeId.equals(findToUpdate.getEmployeeId()))
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.NOT_RESULT_OWNER);

        final ExaminationResult tempResult = findToUpdate;
        List<ExaminationResultDetail> details = request.getDetails().stream().map(detail -> {
                                            ExaminationResultDetail entity = resultDetailMapperUtil.mapCreateRequestToDetail(detail);
                                            entity.setResult(tempResult);
                                            return entity;
                                        }).collect(Collectors.toList());

        findToUpdate.setDetails(details);
        findToUpdate.setExaminatedAt(LocalDateTime.now());
        findToUpdate.setTreatment(request.getTreatment());
        findToUpdate = examinationResultRepository.save(findToUpdate);

        //Gửi thông tin tạo đoạn chat giữa bệnh nhân và bác sĩ
        CreateRelationShipCommonRequest relationShipCommonRequest = CreateRelationShipCommonRequest.builder()
                .doctorId(employeeId)
                .patientId(findToUpdate.getPatientId())
                .build();
        kafkaTemplate.send(CREATE_RELATION_SHIP_TOPIC, relationShipCommonRequest);


        return resultMapperUtil.mapEntityToResponse(findToUpdate);
    }

    public List<ExaminationResultResponse> findHistoriesOfPatient(){
        UUID patientId = auditingUtil.getUserLogged().getId();
        return resultMapperUtil.mapEntitiesToResponses(examinationResultRepository.findByPatientIdOrderByCreatedAtDesc(patientId));
    }

    public List<ExaminationResultResponse> findByPatientId(UUID patientId){
        return resultMapperUtil.mapEntitiesToResponses(examinationResultRepository.findExaminedHistoriesOfPatient(patientId));
    }

    public List<ExaminationResultResponse> getExaminedResultTodayOfDoctor(){
        UUID doctorId = auditingUtil.getUserLogged().getId();
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0);
        LocalDateTime end = start.plusDays(1);
        return resultMapperUtil.mapEntitiesToResponses(examinationResultRepository.findExaminedResultTodayOfDoctor(doctorId, start, end));
    }
}