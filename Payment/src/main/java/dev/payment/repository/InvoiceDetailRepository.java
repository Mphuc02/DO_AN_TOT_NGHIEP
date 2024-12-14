package dev.payment.repository;

import dev.payment.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, UUID> {
    void deleteAllByInvoiceId(UUID invoiceId);
}