package dev.patient.repository;

import dev.patient.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByExaminationResultId(UUID examinationResultId);
    boolean existsByPatientIdAndAppointmentDate(UUID patientId, LocalDate date);
    List<Appointment> findByAppointmentDateAndIsExamined(LocalDate today, boolean isExamined);
    List<Appointment> findByAppointmentDateBetweenAndPatientIdOrderByAppointmentDate(LocalDate start, LocalDate end, UUID patientId);
}