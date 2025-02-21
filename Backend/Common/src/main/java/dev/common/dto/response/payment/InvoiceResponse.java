package dev.common.dto.response.payment;

import dev.common.dto.response.payment.InvoiceDetailResponse;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceResponse {
    private UUID id;
    private UUID patientId;
    private UUID employeeId;
    private BigDecimal examinationCost;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private String method;
    private List<InvoiceDetailResponse> details;
}