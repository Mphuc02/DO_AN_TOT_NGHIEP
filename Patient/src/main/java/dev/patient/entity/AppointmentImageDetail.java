package dev.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_appointment_image_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentImageDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "image", columnDefinition = "text")
    private String image;

    @Column(name = "processed_image", columnDefinition = "text")
    private String processedImage;

    @ManyToOne(fetch = FetchType.LAZY)
    private Appointment appointment;
}