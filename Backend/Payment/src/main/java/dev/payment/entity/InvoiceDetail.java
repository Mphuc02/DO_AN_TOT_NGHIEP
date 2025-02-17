package dev.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_invoice_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID medicineId;
    private Integer quantity;
    private BigDecimal price;

    @ManyToOne
    private Invoice invoice;
}