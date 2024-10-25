package dev.medicine.util;

import dev.medicine.dto.request.create.CreateSupplierRequest;
import dev.medicine.dto.request.update.UpdateSupplierRequest;
import dev.common.dto.response.medicine.SupplierResponse;
import dev.medicine.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierMapperUtil {
    Supplier mapCreateRequestToEntity(CreateSupplierRequest request);
    void mapUpdateRequestToEntity(UpdateSupplierRequest request, @MappingTarget Supplier supplier);
    SupplierResponse mapEntityToResponse(Supplier supplier);
    List<SupplierResponse> mapEntitiesToResponses(List<Supplier> suppliers);
}