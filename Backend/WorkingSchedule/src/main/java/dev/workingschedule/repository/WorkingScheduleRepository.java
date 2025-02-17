package dev.workingschedule.repository;

import dev.workingschedule.entity.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, UUID> {
    @Query("""
            Select ws from WorkingSchedule ws
            where
            (:startDate is null or ws.date >= :startDate) and
            (:endDate is null or ws.date <= :endDate) and
            (:roomId is null or ws.roomId = :roomId) and
            (:employeeId is null or ws.employeeId = :employeeId)
        """)
    List<WorkingSchedule> searchWorkingSchedule(LocalDate startDate, LocalDate endDate, UUID roomId, UUID employeeId);

    List<WorkingSchedule> findByEmployeeIdAndDateBetweenOrderByDate(UUID employeeId, LocalDate thisMonth, LocalDate nextMonth);
    WorkingSchedule findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    List<WorkingSchedule> getByDate(LocalDate localDate);
}