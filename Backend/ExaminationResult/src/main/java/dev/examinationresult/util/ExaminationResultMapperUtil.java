package dev.examinationresult.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.response.examination_result.ExaminationResultResponse;
import dev.examinationresult.entity.ExaminationResult;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationResultMapperUtil {
    ExaminationResult mapCreateRequestToEntity(CreateExaminationResultCommonRequest request);
    CreateInvoiceCommonRequest mapEntityToCreateInvoiceRequest(ExaminationResult result);
    ExaminationResultResponse mapEntityToResponse(ExaminationResult result);
    List<ExaminationResultResponse> mapEntitiesToResponses(List<ExaminationResult> entities);
}