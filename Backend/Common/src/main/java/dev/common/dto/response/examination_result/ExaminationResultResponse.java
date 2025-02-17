package dev.common.dto.response.examination_result;

import dev.common.dto.response.employee.EmployeeResponse;
import dev.common.dto.response.patient.PatientResponse;
import dev.common.dto.response.working_schedule.WorkingScheduleResponse;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationResultResponse {
    private UUID id;
    private UUID patientId;
    private UUID workingScheduleId;
    private UUID employeeId;    
    private String treatment;
    private UUID appointmentId;
    private LocalDateTime examinatedAt;
    private Integer examinedNumber;
    private String symptom;
    private List<ExaminationResultDetailResponse> details;
    private LocalDateTime createdAt;
    private PatientResponse patient;
    private EmployeeResponse employee;
    private WorkingScheduleResponse workingSchedule;
}