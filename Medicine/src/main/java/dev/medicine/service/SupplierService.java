package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.medicine.dto.request.CreateSupplierRequest;
import dev.medicine.dto.request.UpdateSupplierRequest;
import dev.medicine.dto.response.SupplierResponse;
import dev.medicine.entity.Supplier;
import dev.medicine.repository.SupplierRepository;
import dev.medicine.util.SupplierMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapperUtil supplierMapperUtil;

    public List<SupplierResponse> getAll(){
        return supplierMapperUtil.mapEntitiesToResponses(supplierRepository.findAll());
    }

    @Transactional
    public SupplierResponse create(CreateSupplierRequest request){
        Supplier supplier = supplierMapperUtil.mapCreateRequestToEntity(request);
        supplier = supplierRepository.save(supplier);
        supplier.getAddress().setSupplier(supplier);
        return supplierMapperUtil.mapEntityToResponse(supplier);
    }

    @Transactional
    public SupplierResponse update(UpdateSupplierRequest request, UUID id){
        Supplier supplier = supplierRepository.findById(id)
                                    .orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.SUPPLIER_NOT_FOUND));
        supplierMapperUtil.mapUpdateRequestToEntity(request, supplier);
        supplier = supplierRepository.save(supplier);
        return supplierMapperUtil.mapEntityToResponse(supplier);
    }
}