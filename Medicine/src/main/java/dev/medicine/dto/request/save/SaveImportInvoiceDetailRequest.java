package dev.medicine.dto.request.save;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveImportInvoiceDetailRequest {
    private UUID medicineId;
    private Integer quantity;
}