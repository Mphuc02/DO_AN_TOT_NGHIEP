package dev.medicine.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer quantity;
    private OriginResponse origin;
    private BigDecimal price;
}