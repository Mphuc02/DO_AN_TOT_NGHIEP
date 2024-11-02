package dev.examinationresult.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.client.WorkingScheduleClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.response.working_schedule.WorkingScheduleResponse;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.model.AuthenticatedUser;
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
    private final ExaminationResultDetailMapperUtil resultDetailUtil;
    private final WorkingScheduleClient workingScheduleClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(KafkaTopicsConstrant.CREATED_EXAMINATION_RESULT_SUCCESS_TOPIC)
    private String CREATED_EXAMINATION_RESULT_SUCCESS;

    @Value(KafkaTopicsConstrant.APPOINTMENT_HAD_BEEN_EXAMINED_TOPIC)
    private String APPOINTMENT_HAD_BEEN_EXAMINED;

    public ExaminationResultResponse getById(UUID id){
        ExaminationResult findById = examinationResultRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(EXAMINATION_RESULT_EXCEPTION.RESULT_NOT_FOUND));
        return resultMapperUtil.mapEntityToResponse(findById);
    }

    @KafkaListener(topics = CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC,
                    groupId = EXAMINATION_RESULT_GROUP)
    public void create(CreateExaminationResultCommonRequest request){
        log.info("receive request create examination result from kafka");
        ExaminationResult result = resultMapperUtil.mapCreateRequestToEntity(request);
        result.setCreatedAt(LocalDateTime.now());
        WorkingScheduleResponse schedule = workingScheduleClient.getById(request.getWorkingScheduleId());
        result.setEmployeeId(schedule.getEmployeeId());
        result = examinationResultRepository.save(result);

        CreateInvoiceCommonRequest createInvoiceRequest = resultMapperUtil.mapEntityToCreateInvoiceRequest(result);
        kafkaTemplate.send(CREATED_EXAMINATION_RESULT_SUCCESS, createInvoiceRequest);

        //If has appointment, update isExamined of appointment
        if(request.getAppointmentId() != null){
            kafkaTemplate.send(APPOINTMENT_HAD_BEEN_EXAMINED, request.getAppointmentId());
        }
    }

    @Transactional
    public ExaminationResultResponse update(UpdateExaminationResultRequest request, UUID id){
        ExaminationResult findToUpdate = examinationResultRepository.findById(id)
                                                        .orElseThrow(() ->
                                                                new NotFoundException(EXAMINATION_RESULT_EXCEPTION.RESULT_NOT_FOUND));

        //Todo: Kiểm tra nếu người đã thanh toàn thì sẽ không thể chinh sửa kết quả khám bệnh nữa
        UUID employeeId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if(!employeeId.equals(findToUpdate.getEmployeeId()))
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.NOT_RESULT_OWNER);

        final ExaminationResult tempResult = findToUpdate;
        List<ExaminationResultDetail> details = request.getDetails().stream().map(detail -> {
                                            ExaminationResultDetail entity = resultDetailUtil.mapCreateRequestToDetail(detail);
                                            entity.setResult(tempResult);
                                            return entity;
                                        }).collect(Collectors.toList());

        findToUpdate.setDetails(details);
        findToUpdate.setExaminatedAt(LocalDateTime.now());
        findToUpdate.setTreatment(request.getTreatment());
        findToUpdate = examinationResultRepository.save(findToUpdate);
        return resultMapperUtil.mapEntityToResponse(findToUpdate);
    }
}