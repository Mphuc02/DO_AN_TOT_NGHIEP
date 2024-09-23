package dev.medicine.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_import_invoice_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportInvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    private BigDecimal price;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private ImportInvoice invoice;
}