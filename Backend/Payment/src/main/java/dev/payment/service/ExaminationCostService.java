package dev.payment.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.payment.dto.request.CreateExaminationCostRequest;
import dev.payment.dto.response.ExaminationCostResponse;
import dev.payment.entity.ExaminationCost;
import dev.payment.repository.ExaminationCostRepository;
import dev.payment.util.ExaminationCostMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExaminationCostService {
    private final ExaminationCostRepository costRepository;
    private final ExaminationCostMapperUtil costMapperUtil;

    public List<ExaminationCostResponse> getAll(){
        return costMapperUtil.mapEntitiesToResponses(costRepository.findAll());
    }

    public ExaminationCostResponse create(CreateExaminationCostRequest request){
        ExaminationCost entity = costMapperUtil.mapCreateRequestToEntity(request);
        entity = costRepository.save(entity);
        return costMapperUtil.mapEntityToResponse(entity);
    }

    public boolean existByAppliedDate(LocalDate date){
        return costRepository.existsByAppliedAt(date);
    }

    public void delete(UUID id){
        ExaminationCost findToDelete = costRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(PAYMENT_EXCEPTION.EXAMINATION_COST_NOT_FOUND));
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if(findToDelete.getAppliedAt().isAfter(yesterday)){
            throw new NotPermissionException(PAYMENT_EXCEPTION.APPLIED_DAY_PASSED);
        }
        costRepository.deleteById(id);
    }
}