package dev.workingschedule.repository;

import dev.workingschedule.entity.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.sql.Date;
import java.util.UUID;

public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, UUID> {
    boolean existsByRoomIdAndDate(UUID roomId, Date date);
}