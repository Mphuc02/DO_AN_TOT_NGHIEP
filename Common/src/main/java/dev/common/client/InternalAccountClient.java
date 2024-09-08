package dev.common.client;

import static dev.common.constant.ApiConstant.AUTHENTICATION_URL.*;
import dev.common.dto.request.CreateNewPatientRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = SERVICE_NAME, path = INTERNAL.URL)
public interface InternalAccountClient {
    @PostMapping()
    boolean saveAccountFrommGreeting(@RequestBody CreateNewPatientRequest request);
}