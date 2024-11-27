package dev.workingschedule.dto.request;

import dev.common.validator.DateAfterTodayValidator;
import dev.common.validator.ExistedExaminationRoomValidator;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldNameConstants
public class SaveWorkingScheduleRequest {
    @ExistedExaminationRoomValidator
    @NotNull(message = "Phòng làm việc không được bỏ trống")
    private UUID roomId;

    @DateAfterTodayValidator
    @NotNull(message = "Ngày làm việc không được bỏ trống")
    private LocalDate date;
}