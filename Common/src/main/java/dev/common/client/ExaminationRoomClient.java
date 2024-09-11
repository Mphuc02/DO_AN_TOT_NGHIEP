package dev.common.client;

import dev.common.constant.ApiConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;

@FeignClient(name = SERVICE_NAME, path = EXAMINATION_ROOM_URL)
public interface ExaminationRoomClient {
    @GetMapping(ApiConstant.HOSPITAL_INFORMATION.CHECK_ROOM_EXIST)
    boolean checkRoomExist(@PathVariable UUID id);
}
