package dev.common.dto.response.payment;

import dev.common.dto.response.medicine.PaidMedicineDetailCommonResponse;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CalculatedMedicinesCost {
    private BigDecimal cost;
    private List<PaidMedicineDetailCommonResponse> details;
}