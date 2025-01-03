package dev.examinationresult.repository;

import dev.examinationresult.entity.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, UUID> {
    Integer countByWorkingScheduleIdAndCreatedAtBetween(UUID workingScheduleId, LocalDateTime start, LocalDateTime end);

    @Query(value = """
        Select er from ExaminationResult er
        where er.employeeId = :doctorId
        and er.createdAt <= :end
        and er.createdAt >= :start
        and er.examinatedAt is null
        order by er.examinedNumber
    """)
    List<ExaminationResult> findWaitingExaminationPatientsOfDoctor(UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<ExaminationResult> findByPatientIdOrderByCreatedAtDesc(UUID patientId);
}