package dev.medicine.dto.request.update;

import dev.medicine.validator.ExistedOriginValidator;
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
public class UpdateMedicineRequest {
    private String name;
    private String description;
    @ExistedOriginValidator
    private UUID originId;

    @NotNull(message = "Gía không được bỏ trống")
    @Min(value = 0, message = "Gía phải lớn hơn hoặc bằng 0")
    private BigDecimal price;
}