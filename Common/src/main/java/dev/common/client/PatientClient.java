package dev.common.client;

import static dev.common.constant.ApiConstant.PATIENT.*;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = SERVICE_NAME, path = PATIENT_URL)
public interface PatientClient {

}