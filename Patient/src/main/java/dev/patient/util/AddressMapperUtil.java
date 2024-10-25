package dev.patient.util;

import dev.common.dto.request.CreatePatientAddressRequest;
import dev.common.dto.response.address.AddressResponse;
import dev.patient.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapperUtil {
    Address createRequestToEntity(CreatePatientAddressRequest request);
    AddressResponse mapEntityToResponse(Address address);
}