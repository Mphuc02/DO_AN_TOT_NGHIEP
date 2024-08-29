package dev.employee.repository;

import dev.employee.entity.FullName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FullNameRepository extends JpaRepository <FullName, UUID> {
}