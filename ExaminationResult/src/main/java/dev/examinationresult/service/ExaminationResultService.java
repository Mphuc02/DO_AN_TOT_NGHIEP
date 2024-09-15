package dev.examinationresult.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.examinationresult.entity.ExaminationResult;
import dev.examinationresult.repository.ExaminationResultRepository;
import dev.examinationresult.util.ExaminationResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExaminationResultService {
    private final ExaminationResultUtil examinationResultUtil;
    private final ExaminationResultRepository examinationResultRepository;

    @KafkaListener(topics = CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC,
                    groupId = EXAMINATION_RESULT_GROUP)
    public void createExaminationResult(CreateExaminationResultCommonRequest request){
        ExaminationResult result = examinationResultUtil.mapCreateRequestToEntity(request);
        examinationResultRepository.save(result);
    }
}