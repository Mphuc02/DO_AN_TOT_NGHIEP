package dev.medicine.util;

import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.entity.ImportInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ImportInvoiceMapperUtil {
    ImportInvoice mapCreateRequestToEntity(CreateImportInvoiceRequest request);

    ImportInvoiceResponse mapEntityToResponse(ImportInvoice invoice);
    List<ImportInvoiceResponse> mapEntitiesToResponses(List<ImportInvoice> invoices);
}