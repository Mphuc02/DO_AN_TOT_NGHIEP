package dev.chat.chatservice.repository;

import dev.chat.chatservice.entity.RelationShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RelationShipRepository extends JpaRepository<RelationShip, UUID> {
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}