package dev.payment.util;

import dev.payment.dto.request.CreateExaminationCostRequest;
import dev.payment.dto.response.ExaminationCostResponse;
import dev.payment.entity.ExaminationCost;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationCostMapperUtil {
    ExaminationCost mapCreateRequestToEntity(CreateExaminationCostRequest request);
    ExaminationCostResponse mapEntityToResponse(ExaminationCost entity);
    List<ExaminationCostResponse> mapEntitiesToResponses(List<ExaminationCost> entities);
}