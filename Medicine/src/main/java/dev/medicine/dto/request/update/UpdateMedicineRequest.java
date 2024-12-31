package dev.medicine.dto.request.update;

import dev.medicine.validator.ExistedOriginValidator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Tên không được bỏ trống")
    private String name;

    @NotEmpty(message = "Mô tả không được bỏ trống")
    private String description;

    @ExistedOriginValidator
    private UUID originId;

    @NotNull(message = "Giá không được bỏ trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;
}