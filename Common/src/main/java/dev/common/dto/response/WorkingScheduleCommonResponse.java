package dev.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorkingScheduleCommonResponse {
    private UUID id;
    private UUID roomId;
    private UUID employeeId;
    private Date date;
}