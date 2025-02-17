package dev.patient.repository;

import dev.patient.entity.FullName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FullNameAddress extends JpaRepository<FullName, UUID> {

}