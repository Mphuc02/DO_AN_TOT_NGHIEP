package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.dto.request.update.UpdateImportInvoiceRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.dto.response.MedicineResponse;
import dev.medicine.entity.ImportInvoice;
import dev.medicine.entity.ImportInvoiceDetail;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.ImportInvoiceDetailRepository;
import dev.medicine.repository.ImportInvoiceRepository;
import dev.medicine.util.ImportInvoiceMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final MedicineService medicineService;

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
    public ImportInvoiceResponse update(UpdateImportInvoiceRequest request, UUID id){
        ImportInvoice invoice = invoiceRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.INVOICE_NOT_FOUND));

        List<ImportInvoiceDetail> oldDetails = invoice.getDetails();
        detailRepository.deleteAll(oldDetails);

        invoiceMapperUtil.mapUpdateRequestToEntity(request, invoice);
        invoice = invoiceRepository.save(invoice);
        handleSaveInvoiceDetail(request.getDetails(), invoice);
        return invoiceMapperUtil.mapEntityToResponse(invoice);
    }

    @Transactional
    public void handleSaveInvoiceDetail(List<SaveImportInvoiceDetailRequest> requests, ImportInvoice invoice){
        HashMap<UUID, ImportInvoiceDetail> details = new HashMap<>();
        requests.forEach(request -> {
            ImportInvoiceDetail get = details.get(request.getMedicineId());
            if(get != null){
                get.setQuantity(get.getQuantity() + request.getQuantity());
            }else {
                MedicineResponse medicine = medicineService.getByID(request.getMedicineId());

                ImportInvoiceDetail detail = ImportInvoiceDetail.builder()
                        .invoice(invoice)
                        .medicine(new Medicine(request.getMedicineId()))
                        .quantity(request.getQuantity())
                        .price(medicine.getPrice())
                        .build();
                details.put(request.getMedicineId(), detail);
            }
        });
        detailRepository.saveAll(details.values());
    }
}