package dev.hospitalinformation.repository;

import dev.hospitalinformation.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DiseaseRepository extends JpaRepository<Disease, UUID> {
}
