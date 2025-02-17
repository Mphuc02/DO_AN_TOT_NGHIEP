package dev.medicine.util;

import dev.medicine.dto.request.save.SaveAddressRequest;
import dev.common.dto.response.address.AddressResponse;
import dev.medicine.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapperUtil {
    Address mapCreateRequestToEntity(SaveAddressRequest request);
    void mapUpdateRequestToEntity(SaveAddressRequest request, @MappingTarget Address address);
    AddressResponse mapEntityToResponse(Address address);
}