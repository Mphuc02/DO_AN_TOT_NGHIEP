package dev.common.dto.request;

import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayMedicineInCashCommonRequest {
    private UUID invoiceId;
    private List<PayMedicineDetailCommonRequest> details;
}