package dev.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExportMedicineDetailCommonRequest {
    private UUID medicineId;
    private Integer quantity;
}