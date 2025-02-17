package dev.common.client;

import static dev.common.constant.ApiConstant.AUTHENTICATION_URL.*;
import static dev.common.constant.ApiConstant.AUTHENTICATION_URL.INTERNAL_URL.CHECK_PHONE_NUMBER_EXIST;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = INTERNAL_URL.URL)
public interface InternalAuthenticationClient {
    @GetMapping(CHECK_PHONE_NUMBER_EXIST)
    boolean checkPhoneNumberNotExist(@RequestParam(name = "number-phone") String phoneNumber,
                                     @RequestParam(name = "user-id") UUID userId);
}