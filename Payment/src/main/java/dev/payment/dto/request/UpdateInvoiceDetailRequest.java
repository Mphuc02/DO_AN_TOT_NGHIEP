package dev.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateInvoiceDetailRequest {
    private UUID id;

    @NotNull(message = "Thuốc không được bỏ trống")
    private UUID medicineId;

    @NotNull(message = "Số lượng không được bỏ trống")
    private Integer quantity;
}