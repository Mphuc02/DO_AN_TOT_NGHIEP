package dev.medicine.repository;

import dev.medicine.entity.ImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, UUID> {
}
