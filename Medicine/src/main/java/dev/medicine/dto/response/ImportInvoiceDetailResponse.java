package dev.medicine.dto.response;

import dev.medicine.entity.Medicine;
import lombok.*;
import java.math.BigInteger;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportInvoiceDetailResponse {
    private UUID id;
    private Medicine medicine;
    private BigInteger price;
    private Integer quantity;
}