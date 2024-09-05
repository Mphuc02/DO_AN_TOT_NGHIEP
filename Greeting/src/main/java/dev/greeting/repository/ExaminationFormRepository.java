package dev.greeting.repository;

import dev.greeting.entity.ExaminationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExaminationFormRepository extends JpaRepository<ExaminationForm, UUID> {
}