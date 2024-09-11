package dev.hospitalinformation.repository;

import dev.hospitalinformation.entity.ExaminationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExaminationRoomRepository extends JpaRepository<ExaminationRoom, UUID> {
}