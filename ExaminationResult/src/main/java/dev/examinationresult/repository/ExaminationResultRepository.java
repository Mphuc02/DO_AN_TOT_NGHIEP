package dev.examinationresult.repository;

import dev.examinationresult.entity.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, UUID> {
}
