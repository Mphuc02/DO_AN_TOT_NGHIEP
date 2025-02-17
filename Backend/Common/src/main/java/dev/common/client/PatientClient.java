package dev.common.client;

import static dev.common.constant.ApiConstant.PATIENT.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = PATIENT_URL)
public interface PatientClient {
    @GetMapping(CHECK_EXIST_PATIENT)
    boolean checkPatientExist(@PathVariable UUID id);
}