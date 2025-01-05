package dev.patient.repository;

import dev.patient.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByExaminationResultId(UUID examinationResultId);
    boolean existsByPatientIdAndAppointmentDate(UUID patientId, LocalDate date);
    List<Appointment> findByAppointmentDateAndIsExaminedOrderByCreatedAt(LocalDate today, boolean isExamined);
    List<Appointment> findByAppointmentDateBetweenAndPatientIdOrderByAppointmentDate(LocalDate start, LocalDate end, UUID patientId);

    @Query(value = """
        Select count(*) from tbl_appointment
        where appointment_date = :today
        and doctor_id = :doctorId
    """, nativeQuery = true)
    int countAppointmentTodayOfDoctor(UUID doctorId, LocalDate today);

    @Query(value = """
        Select count(appointments.id) + 1 from tbl_appointment appointments
        where appointment_date = :today
        and doctor_id = :doctorId
        and id <> :appointmentId
        and created_at <= :appointmentCreatedDate
    """, nativeQuery = true)
    int getOrderNumberOfAppointment(UUID doctorId, UUID appointmentId, LocalDate today, LocalDateTime appointmentCreatedDate);

    Appointment findByExaminationResultId(UUID id);
}