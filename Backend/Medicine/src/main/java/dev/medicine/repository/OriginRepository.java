package dev.medicine.repository;

import dev.medicine.entity.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OriginRepository extends JpaRepository<Origin, UUID> {
}