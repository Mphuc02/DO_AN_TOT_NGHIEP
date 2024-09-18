package dev.medicine.util;

import dev.medicine.dto.request.CreateOriginRequest;
import dev.medicine.dto.request.UpdateOriginRequest;
import dev.medicine.dto.response.OriginResponse;
import dev.medicine.entity.Origin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OriginMapperUtil {
    Origin mapCreateRequestToEntity(CreateOriginRequest request);
    void mapUpdateRequestToEntity(UpdateOriginRequest request, @MappingTarget Origin origin);
    OriginResponse mapEntityToResponse(Origin origin);
    List<OriginResponse> mapEntitiesToResponses(List<Origin> origins);
}