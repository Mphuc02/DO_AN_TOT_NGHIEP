package dev.medicine.util;

import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.request.update.UpdateImportInvoiceRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.entity.ImportInvoice;
import dev.medicine.entity.Supplier;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ImportInvoiceMapperUtil {
    @Mapping(target = "supplier", source = "request.supplierId", qualifiedByName = "mapSupplierFromId")
    @Mapping(target = "details", ignore = true)
    ImportInvoice mapCreateRequestToEntity(CreateImportInvoiceRequest request);

    @Mapping(target = "supplier", source = "request.supplierId", qualifiedByName = "mapSupplierFromId")
    @Mapping(target = "details", ignore = true)
    void mapUpdateRequestToEntity(UpdateImportInvoiceRequest request, @MappingTarget ImportInvoice invoice);

    ImportInvoiceResponse mapEntityToResponse(ImportInvoice invoice);
    List<ImportInvoiceResponse> mapEntitiesToResponses(List<ImportInvoice> invoices);

    @Named(value = "mapSupplierFromId")
    default Supplier mapSupplierFromId(UUID id){
        return new Supplier(id);
    }
}