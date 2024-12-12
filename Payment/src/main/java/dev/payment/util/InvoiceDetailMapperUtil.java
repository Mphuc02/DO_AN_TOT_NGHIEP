package dev.payment.util;

import dev.common.dto.request.ExportMedicineDetailCommonRequest;
import dev.common.dto.response.payment.InvoiceDetailResponse;
import dev.payment.entity.InvoiceDetail;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapperUtil {
    List<InvoiceDetailResponse> mapEntitiesToResponses(List<InvoiceDetail> invoiceDetails);
    List<ExportMedicineDetailCommonRequest> mapEntitiesToMedicineDetailRequest (List<InvoiceDetail> invoiceDetails);
}