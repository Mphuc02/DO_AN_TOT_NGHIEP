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

    @Query(value = """
        Select er from ExaminationResult er
        where er.employeeId = :doctorId
        and er.createdAt <= :end
        and er.createdAt >= :start
        and er.examinatedAt is not null
        order by er.examinedNumber
    """)
    List<ExaminationResult> findExaminedResultTodayOfDoctor(UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<ExaminationResult> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    @Query(value = """
        Select er from ExaminationResult er
        where er.patientId = :patientId
        and er.examinatedAt is not null
        order by er.createdAt desc
    """)
    List<ExaminationResult> findExaminedHistoriesOfPatient(UUID patientId);

    @Query(value = """
        Select e from ExaminationResult e
        where e.employeeId = :doctorId
        and e.createdAt <= :tomorrow
        and e.createdAt >= :today
        order by e.createdAt desc
        limit 1
    """)
    ExaminationResult getLastResultOfDoctor(UUID doctorId, LocalDateTime today, LocalDateTime tomorrow);
}