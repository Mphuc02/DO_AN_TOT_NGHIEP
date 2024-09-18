package dev.medicine.dto.request;

import dev.medicine.validator.ExistedOriginValidator;
import lombok.*;
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
}