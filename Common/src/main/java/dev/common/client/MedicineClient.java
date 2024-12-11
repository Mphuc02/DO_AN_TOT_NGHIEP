package dev.common.client;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;

import dev.common.dto.request.PayMedicineDetailCommonRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = MEDICINE_URL)
public interface MedicineClient {
    @PostMapping(CHECK_MEDICINES_EXIST)
    Set<UUID> checkMedicinesExist(@RequestBody List<UUID> ids);

    @PostMapping(CALCULATE_MEDICINES_COST)
    BigDecimal calculateMedicinesCost(@RequestBody List<PayMedicineDetailCommonRequest> medicines);
}