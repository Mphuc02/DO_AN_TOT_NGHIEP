package dev.payment.util;

import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.request.PayMedicineInCashCommonRequest;
import dev.payment.dto.request.PayInCashRequest;
import dev.payment.dto.response.InvoiceResponse;
import dev.payment.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapperUtil {
    Invoice mapCreateRequestToEntity(CreateInvoiceCommonRequest request);
    List<InvoiceResponse> mapEntitiesToResponses(List<Invoice> invoices);
    PayMedicineInCashCommonRequest mapToPaymentMedicine(PayInCashRequest request);
}