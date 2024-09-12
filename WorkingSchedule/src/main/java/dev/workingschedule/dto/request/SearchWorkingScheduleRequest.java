package dev.workingschedule.dto.request;

import lombok.*;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchWorkingScheduleRequest {
    private Date startDate;
    private Date endDate;
    private UUID roomId;
    private UUID employeeId;
}