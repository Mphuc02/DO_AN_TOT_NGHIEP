package dev.examinationresult.repository;

import dev.examinationresult.entity.AppointmentForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentFormRepository extends JpaRepository<AppointmentForm, UUID> {
}
