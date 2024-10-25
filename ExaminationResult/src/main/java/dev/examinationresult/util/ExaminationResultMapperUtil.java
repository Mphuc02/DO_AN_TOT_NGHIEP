package dev.examinationresult.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.response.examination_result.ExaminationResultResponse;
import dev.examinationresult.entity.ExaminationResult;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationResultMapperUtil {
    ExaminationResultMapperUtil INSTANCE = Mappers.getMapper(ExaminationResultMapperUtil.class);
    ExaminationResult mapCreateRequestToEntity(CreateExaminationResultCommonRequest request);
    CreateInvoiceCommonRequest mapEntityToCreateInvoiceRequest(ExaminationResult result);
    ExaminationResultResponse mapEntityToResponse(ExaminationResult result);
}