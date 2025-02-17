package dev.common.dto.response.working_schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorkingScheduleResponse {
    private UUID id;
    private UUID roomId;
    private UUID employeeId;
    private LocalDate date;
}