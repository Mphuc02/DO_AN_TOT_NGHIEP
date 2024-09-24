package dev.examinationresult.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.examinationresult.dto.response.ExaminationResultResponse;
import dev.examinationresult.entity.ExaminationResult;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationResultUtil {
    ExaminationResultUtil INSTANCE = Mappers.getMapper(ExaminationResultUtil.class);
    ExaminationResult mapCreateRequestToEntity(CreateExaminationResultCommonRequest request);
    ExaminationResultResponse mapEntityToResponse(ExaminationResult result);
}