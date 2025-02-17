package dev.patient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tbl_appointment_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID diseaseId;

    @ManyToOne
    private Appointment appointment;
}