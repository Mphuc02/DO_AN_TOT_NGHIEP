package dev.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_import_invoice")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID employeeId;
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    private List<ImportInvoiceDetail> details;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
}