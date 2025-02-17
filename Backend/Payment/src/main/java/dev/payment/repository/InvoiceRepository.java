package dev.payment.repository;

import dev.payment.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    @Query(value = """
        Select i from Invoice i
        where i.paidAt is null
        order by i.createdAt desc
    """)
    Page<Invoice> findUnPaidInvoice(Pageable pageable);
}