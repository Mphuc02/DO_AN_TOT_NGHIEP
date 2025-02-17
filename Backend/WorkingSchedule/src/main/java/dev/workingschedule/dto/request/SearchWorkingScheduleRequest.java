package dev.workingschedule.dto.request;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchWorkingScheduleRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID roomId;
    private UUID employeeId;
}