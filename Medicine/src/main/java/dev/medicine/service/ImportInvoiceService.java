package dev.medicine.service;

import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.entity.ImportInvoice;
import dev.medicine.entity.ImportInvoiceDetail;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.ImportInvoiceDetailRepository;
import dev.medicine.repository.ImportInvoiceRepository;
import dev.medicine.repository.MedicineRepository;
import dev.medicine.util.ImportInvoiceMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportInvoiceService {
    private final ImportInvoiceRepository invoiceRepository;
    private final ImportInvoiceMapperUtil invoiceMapperUtil;
    private final ImportInvoiceDetailRepository detailRepository;
    private final MedicineRepository medicineRepository;

    public List<ImportInvoiceResponse> getAll(){
        return invoiceMapperUtil.mapEntitiesToResponses(invoiceRepository.findAll());
    }

    @Transactional
    public ImportInvoiceResponse create(CreateImportInvoiceRequest request){
        ImportInvoice invoice = invoiceMapperUtil.mapCreateRequestToEntity(request);
        invoice = invoiceRepository.save(invoice);
        handleSaveInvoiceDetail(request.getDetails(), invoice);
        return invoiceMapperUtil.mapEntityToResponse(invoice);
    }

    @Transactional
    public void handleSaveInvoiceDetail(List<SaveImportInvoiceDetailRequest> requests, ImportInvoice invoice){
        HashMap<UUID, ImportInvoiceDetail> detailsMap = new HashMap<>();
        List<Medicine> medicines = new ArrayList<>();
        requests.forEach(request -> {
            ImportInvoiceDetail get = detailsMap.get(request.getMedicineId());
            if(get != null){
                get.setQuantity(get.getQuantity() + request.getQuantity());
                get.getMedicine().increaseQuantity(request.getQuantity());
            }else {
                Medicine medicine = medicineRepository.findById(request.getMedicineId()).get();
                medicine.increaseQuantity(request.getQuantity());
                medicines.add(medicine);

                ImportInvoiceDetail detail = ImportInvoiceDetail.builder()
                        .invoice(invoice)
                        .medicine(medicine)
                        .quantity(request.getQuantity())
                        .price(request.getPrice())
                        .build();
                detailsMap.put(request.getMedicineId(), detail);
            }
        });
        List<ImportInvoiceDetail> details = detailRepository.saveAll(detailsMap.values());
        invoice.setDetails(details);
        medicineRepository.saveAll(medicines);
    }
}