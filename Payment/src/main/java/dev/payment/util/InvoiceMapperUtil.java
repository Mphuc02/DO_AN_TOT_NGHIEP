package dev.payment.util;

import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.payment.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapperUtil {
    Invoice mapCreateRequestToEntity(CreateInvoiceCommonRequest request);
}