package dev.examinationresult.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_appointment_form")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID workingScheduleId;
    private UUID employeeId;
    private UUID patientId;
    private LocalDateTime createdAt;
    private LocalDate appointingAt;
}