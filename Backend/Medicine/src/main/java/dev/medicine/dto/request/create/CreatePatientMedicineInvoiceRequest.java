package dev.medicine.dto.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePatientMedicineInvoiceRequest {
    @NotNull(message = "Id hóa đơn không được bỏ trống")
    private UUID invoiceId;

    @Valid
    @NotEmpty(message = "Chi tiết hóa đơn không được bỏ trống")
    List<CreatePatientMedicineInvoiceDetailRequest> details;
}