package dev.medicine.repository;

import dev.medicine.entity.ImportInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImportInvoiceDetailRepository extends JpaRepository<ImportInvoiceDetail, UUID> {
    List<Optional<ImportInvoiceDetail>> findAllByIdInAndInvoiceId(List<UUID> detailsIds, UUID invoiceId);
    boolean existsAllByIdInAndInvoiceId(List<UUID> detailIds, UUID invoiceId);
}