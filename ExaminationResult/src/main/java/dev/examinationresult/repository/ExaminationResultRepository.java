package dev.examinationresult.repository;

import dev.examinationresult.entity.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, UUID> {
    Integer countByWorkingScheduleIdAndCreatedAtBetween(UUID workingScheduleId, LocalDateTime start, LocalDateTime end);
}