package dev.examinationresult.repository;

import dev.examinationresult.entity.MedicineConsultationFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicineConsultationFormDetailRepository extends JpaRepository<MedicineConsultationFormDetail, UUID> {
}
