package dev.examinationresult.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_medicine_consultation_form")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineConsultationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime createdAt;
    private UUID employeeId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "form", fetch = FetchType.LAZY)
    private List<MedicineConsultationFormDetail> details;
}