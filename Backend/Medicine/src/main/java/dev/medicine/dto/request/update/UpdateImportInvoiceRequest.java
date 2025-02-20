package dev.medicine.dto.request.update;

import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.validator.ExistedSupplierValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateImportInvoiceRequest {
    @NotEmpty(message = "Chi tiết hóa đơn không được bỏ trống")
    @Valid
    private List<SaveImportInvoiceDetailRequest> details;

    @ExistedSupplierValidator
    private UUID supplierId;
}