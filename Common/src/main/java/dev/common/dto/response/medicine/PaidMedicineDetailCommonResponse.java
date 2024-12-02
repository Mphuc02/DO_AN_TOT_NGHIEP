package dev.common.dto.response.medicine;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaidMedicineDetailCommonResponse {
    private UUID medicineId;
    private Integer quantity;
    private BigDecimal price;
}