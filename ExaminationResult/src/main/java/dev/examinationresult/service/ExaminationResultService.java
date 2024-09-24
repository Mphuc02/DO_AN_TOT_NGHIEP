package dev.examinationresult.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.client.WorkingScheduleClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.model.AuthenticatedUser;
import dev.examinationresult.dto.request.UpdateExaminationResultRequest;
import dev.examinationresult.dto.response.ExaminationResultResponse;
import dev.examinationresult.entity.ExaminationResult;
import dev.examinationresult.entity.ExaminationResultDetail;
import dev.examinationresult.repository.ExaminationResultRepository;
import dev.examinationresult.util.ExaminationResultDetailUtil;
import dev.examinationresult.util.ExaminationResultUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExaminationResultService {
    private final ExaminationResultUtil resultUtil;
    private final ExaminationResultRepository examinationResultRepository;
    private final ExaminationResultDetailUtil resultDetailUtil;
    private final WorkingScheduleClient workingScheduleClient;

    public ExaminationResultResponse getById(UUID id){
        ExaminationResult findById = examinationResultRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(EXAMINATION_RESULT_EXCEPTION.RESULT_NOT_FOUND));
        return resultUtil.mapEntityToResponse(findById);
    }

    @KafkaListener(topics = CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC,
                    groupId = EXAMINATION_RESULT_GROUP)
    public void create(CreateExaminationResultCommonRequest request){
        ExaminationResult result = resultUtil.mapCreateRequestToEntity(request);
        result.setCreatedAt(LocalDateTime.now());
        WorkingScheduleCommonResponse schedule = workingScheduleClient.getById(request.getWorkingScheduleId());
        result.setEmployeeId(schedule.getEmployeeId());
        examinationResultRepository.save(result);
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
        return resultUtil.mapEntityToResponse(findToUpdate);
    }
}