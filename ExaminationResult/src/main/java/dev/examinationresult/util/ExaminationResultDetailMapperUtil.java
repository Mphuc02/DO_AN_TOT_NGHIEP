package dev.examinationresult.util;

import dev.examinationresult.dto.request.CreateExaminationResultDetailRequest;
import dev.examinationresult.entity.ExaminationResultDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExaminationResultDetailMapperUtil {
    ExaminationResultMapperUtil INSTANCE = Mappers.getMapper(ExaminationResultMapperUtil.class);
    ExaminationResultDetail mapCreateRequestToDetail(CreateExaminationResultDetailRequest request);
}