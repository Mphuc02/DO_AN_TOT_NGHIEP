package dev.payment.dto.request;

import dev.common.dto.request.PayMedicineDetailCommonRequest;
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
public class PayInCashRequest {
    @Valid
    private List<PayMedicineDetailCommonRequest> details;
}