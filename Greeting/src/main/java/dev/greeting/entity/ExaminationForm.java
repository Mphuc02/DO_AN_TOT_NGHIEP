package dev.greeting.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_examination_form")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID patientId;
    private UUID employeeId;
    private Integer numberCall;

    private UUID workingScheduleId;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String symptom;
}