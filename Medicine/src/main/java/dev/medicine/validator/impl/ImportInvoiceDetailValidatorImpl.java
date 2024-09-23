package dev.medicine.validator.impl;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.repository.MedicineRepository;
import dev.medicine.validator.ImportInvoiceDetailsValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ImportInvoiceDetailValidatorImpl implements ConstraintValidator<ImportInvoiceDetailsValidator, List<SaveImportInvoiceDetailRequest>> {
    private final MedicineRepository medicineRepository;

    @Override
    public boolean isValid(List<SaveImportInvoiceDetailRequest> details, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(details))
            return true;


        HashMap<Integer, HashMap<String, String>> detailErrors = new HashMap<>();
        for(int index = 0; index < details.size(); index++){
            HashMap<String, String> errors = new HashMap<>();

            if(details.get(index).getMedicineId() == null){
                errors.put("medicine", "Thuốc không được bỏ trống");
            }else{
                boolean existedMedicine = medicineRepository.existsById(details.get(index).getMedicineId());
                if(!existedMedicine)
                    errors.put("medicine", "Thuốc không tồn tại");
            }

            if(details.get(index).getQuantity() == null){
                errors.put("quantity", "Số lượng không được bỏ trống");
            }else {
                if(details.get(index).getQuantity() < 1)
                    errors.put("quantity", "Số lượng phải ít nhất là 1");
            }

            if(!errors.isEmpty())
                detailErrors.put(index, errors);
        }
        if(!detailErrors.isEmpty())
            throw new ObjectIllegalArgumentException((Map<Object, Object>) (Object) detailErrors, MEDICINE_EXCEPTION.FAIL_VALIDATION_INVOICE);

        return true;
    }
}