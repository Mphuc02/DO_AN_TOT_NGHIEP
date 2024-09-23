package dev.medicine.dto.request.create;

import dev.medicine.dto.request.save.SaveImportInvoiceDetailRequest;
import dev.medicine.validator.ExistedSupplierValidator;
import dev.medicine.validator.ImportInvoiceDetailsValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateImportInvoiceRequest {
    @NotEmpty(message = "Chi tiết hóa đơn không được bỏ trống")
    @Valid
    @ImportInvoiceDetailsValidator
    private List<SaveImportInvoiceDetailRequest> details;

    @NotNull(message = "Nhà cung cấp không được bỏ trống")
    @ExistedSupplierValidator
    private UUID supplierId;
}