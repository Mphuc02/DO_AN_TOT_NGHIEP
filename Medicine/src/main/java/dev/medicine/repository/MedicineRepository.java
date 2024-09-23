package dev.medicine.repository;

import dev.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
}