package dev.common.dto.response.medicine;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportInvoiceResponse {
    private UUID id;
    private UUID employeeId;
    private LocalDateTime createdAt;
    private List<ImportInvoiceDetailResponse> details;
    private SupplierResponse supplier;
}