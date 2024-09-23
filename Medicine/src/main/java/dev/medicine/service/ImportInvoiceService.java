package dev.medicine.service;

import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.entity.ImportInvoice;
import dev.medicine.entity.ImportInvoiceDetail;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.ImportInvoiceDetailRepository;
import dev.medicine.repository.ImportInvoiceRepository;
import dev.medicine.util.ImportInvoiceMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportInvoiceService {
    private final ImportInvoiceRepository invoiceRepository;
    private final ImportInvoiceMapperUtil invoiceMapperUtil;
    private final ImportInvoiceDetailRepository detailRepository;

    public List<ImportInvoiceResponse> getAll(){
        return invoiceMapperUtil.mapEntitiesToResponses(invoiceRepository.findAll());
    }

    @Transactional
    public ImportInvoiceResponse create(CreateImportInvoiceRequest request){
        ImportInvoice invoice = invoiceMapperUtil.mapCreateRequestToEntity(request);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapperUtil.mapEntityToResponse(invoice);
    }

    private void handleCreateInvoiceDetail(List<SaveImportInvoiceDetailRequest> requests, ImportInvoice invoice){
        HashMap<Pair<UUID, UUID>, Integer> keys = new HashMap<>();
        requests.forEach(request -> {
            ImportInvoiceDetail detail = ImportInvoiceDetail.builder()
                    .invoice(invoice)
                    .medicine(new Medicine(request.getMedicineId()))
                    .quantity(request.getQuantity())
                    .build();
        });
    }
}