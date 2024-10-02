package dev.medicine.dto.request.save;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveImportInvoiceDetailRequest {
    @NotNull(message = "Thuốc không được bỏ trống")
    private UUID medicineId;

    @NotNull(message = "Số lượng không được bỏ trống")
    private Integer quantity;

    @NotNull(message = "Gía không được bỏ trống")
    @Min(value = 1, message = "Gía phải lơn hơn 0")
    private BigDecimal price;
}