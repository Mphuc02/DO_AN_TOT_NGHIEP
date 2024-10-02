package dev.medicine.repository;

import dev.medicine.entity.ExportInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExportInvoiceDetailRepository extends JpaRepository<ExportInvoiceDetail, UUID> {
}