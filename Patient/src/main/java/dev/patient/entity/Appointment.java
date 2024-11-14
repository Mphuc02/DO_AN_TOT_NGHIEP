package dev.patient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_appointment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID patientId;
    private UUID doctorId;
    private String description;
    private UUID examinationResultId;
    private Boolean isExamined = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointment", fetch = FetchType.LAZY)
    private List<AppointmentDetail> details;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointment", fetch = FetchType.LAZY)
    private List<AppointmentImageDetail> images;

    private LocalDate appointmentDate;
    private LocalDateTime createdAt;
}