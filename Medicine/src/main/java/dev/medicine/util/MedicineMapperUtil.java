package dev.medicine.util;

import dev.medicine.dto.request.CreateMedicineRequest;
import dev.medicine.dto.request.UpdateMedicineRequest;
import dev.medicine.dto.response.MedicineResponse;
import dev.medicine.entity.Medicine;
import dev.medicine.entity.Origin;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicineMapperUtil {
    @Mapping(target = "origin", source = "request.originId", qualifiedByName = "mapOriginFromId")
    Medicine mapCreateRequestToEntity(CreateMedicineRequest request);
    @Mapping(target = "origin", source = "request.originId", qualifiedByName = "mapOriginFromId")
    void mapUpdateRequestToEntity(UpdateMedicineRequest request, @MappingTarget Medicine medicine);

    MedicineResponse mapEntityToResponse(Medicine medicine);
    List<MedicineResponse> mapEntitiesFromResponses(List<Medicine> medicines);

    @Named("mapOriginFromId")
    default Origin mapOriginFromId(UUID id){
        return Origin.builder()
                .id(id)
                .build();
    }
}