package dev.examinationresult.repository;

import dev.examinationresult.entity.MedicineConsultationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MedicineConsultationFormRepository extends JpaRepository<MedicineConsultationForm, UUID> {

}