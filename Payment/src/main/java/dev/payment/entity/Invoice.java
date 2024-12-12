package dev.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_invoice")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Invoice {
    @Id
    private UUID id;
    private UUID patientId;
    private UUID employeeId;
    private BigDecimal examinationCost;
    private LocalDateTime createdAt;
    private String onlinePaymentStatus;
    private LocalDateTime paidAt;
    private String paymentMethod;
    private BigDecimal totalCost;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<InvoiceDetail> details;
}