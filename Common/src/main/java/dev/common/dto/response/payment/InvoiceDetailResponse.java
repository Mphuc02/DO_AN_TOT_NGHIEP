package dev.common.dto.response.payment;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceDetailResponse {
    private UUID id;
    private UUID medicineId;
    private Integer quantity;
    private BigDecimal price;
}