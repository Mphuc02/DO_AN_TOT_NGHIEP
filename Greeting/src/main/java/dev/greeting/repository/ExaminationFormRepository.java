package dev.greeting.repository;

import dev.greeting.entity.ExaminationForm;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExaminationFormRepository extends JpaRepository<ExaminationForm, UUID> {
    List<ExaminationForm> findByCreatedAtIsBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

}