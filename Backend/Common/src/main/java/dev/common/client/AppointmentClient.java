package dev.common.client;

import dev.common.dto.request.GetOrderNumberAppointmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;
import static dev.common.constant.ApiConstant.PATIENT.*;

@FeignClient(name = SERVICE_NAME, path = APPOINTMENT_URL)
public interface AppointmentClient {
    @PostMapping(COUNT_APPOINTMENT_TODAY_OF_DOCTOR)
    Integer countAppointmentTodayOfDoctor(@RequestBody UUID doctorId);

    @PostMapping(GET_ORDER_NUMBER_OF_APPOINTMENT)
    Integer getOrderNumberOfAppointment(@RequestBody GetOrderNumberAppointmentRequest request);
}