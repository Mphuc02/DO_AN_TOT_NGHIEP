package dev.common.dto.request;

import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExportMedicineCommonRequest {
    private UUID invoiceId;
    private List<ExportMedicineDetailCommonRequest> details;
}