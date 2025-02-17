package dev.common.dto.response.medicine;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaidMedicineCommonResponse {
    private UUID invoiceId;
    private List<PaidMedicineDetailCommonResponse> details;
    private boolean isPaidOnline = false;
}