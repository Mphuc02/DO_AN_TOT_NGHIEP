package dev.examinationresult.repository;

import dev.examinationresult.entity.ExaminationResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExaminationResultDetailRepository extends JpaRepository<ExaminationResultDetail, UUID> {
}
