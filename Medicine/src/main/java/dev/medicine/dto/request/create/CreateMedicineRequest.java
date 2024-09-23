package dev.medicine.dto.request.create;

import dev.medicine.validator.ExistedOriginValidator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateMedicineRequest {
    @NotEmpty(message = "Tên thuốc không được bỏ trống")
    private String name;

    @NotEmpty(message = "Mô tả của thuốc không được bỏ trống")
    private String description;

    @NotNull(message = "Nguồn gốc không được bỏ trống")
    @ExistedOriginValidator
    private UUID originId;
}