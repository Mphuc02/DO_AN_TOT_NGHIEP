package dev.payment.dto.request;

import dev.common.dto.request.ExportMedicineDetailCommonRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    @Valid
    private List<ExportMedicineDetailCommonRequest> details;
}