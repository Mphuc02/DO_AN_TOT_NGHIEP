package dev.medicine.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePatientMedicineInvoiceDetailRequest {
    @NotNull(message = "Thuốc không được bỏ trống")
    private UUID medicineId;

    @NotNull(message = "Số lượng không được bỏ trống")
    private Integer quantity;
}