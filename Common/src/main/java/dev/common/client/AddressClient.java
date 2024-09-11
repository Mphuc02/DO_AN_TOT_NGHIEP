package dev.common.client;

import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;

import dev.common.dto.request.CheckAddressRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = SERVICE_NAME, path = PROVINCE_URL)
public interface AddressClient {
    @PostMapping(CHECK_ADDRESS)
    boolean checkAddress(@RequestBody CheckAddressRequest request);
}