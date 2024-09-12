package dev.common.client;

import static dev.common.constant.ApiConstant.WORKING_SCHEDULE_URL.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = URL)
public interface WorkingScheduleClient {
    @GetMapping(CHECk_SCHEDULE_TODAY)
    boolean checkScheduleIsToday(@PathVariable UUID id);
}