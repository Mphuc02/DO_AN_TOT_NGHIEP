package dev.medicine.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportInvoiceDetailResponse {
    private UUID id;
    private MedicineResponse medicine;
    private BigDecimal price;
    private Integer quantity;
}