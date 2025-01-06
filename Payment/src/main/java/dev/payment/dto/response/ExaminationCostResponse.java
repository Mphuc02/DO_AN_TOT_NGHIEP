package dev.payment.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationCostResponse {
    private UUID id;
    private BigDecimal cost;
    private LocalDate appliedAt;
}