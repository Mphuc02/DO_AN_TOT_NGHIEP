package dev.patient.repository;

import dev.patient.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByPatientIdAndAppointmentDate(UUID patientId, LocalDate date);
    List<Appointment> findByAppointmentDate(LocalDate today);
}