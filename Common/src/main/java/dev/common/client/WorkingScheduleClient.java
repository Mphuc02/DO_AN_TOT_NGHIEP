package dev.common.client;

import static dev.common.constant.ApiConstant.WORKING_SCHEDULE_URL.*;
import dev.common.dto.response.WorkingScheduleCommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = URL)
public interface WorkingScheduleClient {
    @GetMapping(GET_SCHEDULE_TODAY_OF_EMPLOYEE)
    WorkingScheduleCommonResponse getScheduleTodayOfEmployee(@PathVariable UUID id);

    @GetMapping(ID)
    WorkingScheduleCommonResponse getById(@PathVariable UUID id);

    @GetMapping(CHECK_SCHEDULE_TODAY)
    boolean checkScheduleIsToday(@PathVariable UUID id);
}