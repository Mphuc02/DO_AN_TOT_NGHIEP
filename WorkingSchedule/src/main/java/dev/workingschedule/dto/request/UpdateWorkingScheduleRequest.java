package dev.workingschedule.dto.request;

import dev.common.validator.DateAfterTodayValidator;
import dev.common.validator.ExistedExaminationRoomValidator;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateWorkingScheduleRequest {
    @ExistedExaminationRoomValidator
    private UUID roomId;

    @DateAfterTodayValidator
    private Date date;
}