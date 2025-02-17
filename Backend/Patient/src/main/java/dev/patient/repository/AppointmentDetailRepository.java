package dev.patient.repository;

import dev.patient.entity.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, UUID> {
    List<AppointmentDetail> findByAppointmentId(UUID id);
}