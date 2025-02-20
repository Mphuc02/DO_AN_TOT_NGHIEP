package dev.hospitalinformation.util;

import dev.common.dto.response.insformation.DiseaseResponse;
import dev.hospitalinformation.dto.request.CreateDiseaseRequest;
import dev.hospitalinformation.dto.request.UpdateDiseaseRequest;
import dev.hospitalinformation.entity.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiseaseUtil {
    DiseaseUtil INSTANCE = Mappers.getMapper(DiseaseUtil.class);
    Disease mapCreateRequestToEntity(CreateDiseaseRequest request);
    DiseaseResponse mapEntityToResponse(Disease disease);
    List<DiseaseResponse> mapEntitiesToResponses(List<Disease> diseases);
    void mapUpdateRequestToEntity(UpdateDiseaseRequest request, @MappingTarget Disease disease);
}