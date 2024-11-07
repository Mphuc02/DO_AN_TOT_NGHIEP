package dev.patient.repository;

import dev.patient.entity.AppointmentImageDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AppointmentImageDetailRepository extends JpaRepository<AppointmentImageDetail, UUID> {
    List<AppointmentImageDetail> findByAppointmentId(UUID id);
}