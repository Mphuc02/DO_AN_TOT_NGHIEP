package dev.examinationresult.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_medicine_consultation_form_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineConsultationFormDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID medicineId;
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MedicineConsultationForm form;
}