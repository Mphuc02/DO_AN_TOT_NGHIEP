package dev.chat.chatservice.repository;

import dev.chat.chatservice.entity.RelationShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RelationShipRepository extends JpaRepository<RelationShip, UUID> {
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
    Page<RelationShip> findByDoctorIdOrderByLastContactDesc(UUID doctorId , Pageable pageable);
    Page<RelationShip> findByPatientIdOrderByLastContactDesc(UUID patientId, Pageable pageable);
    RelationShip findByPatientIdAndDoctorId(UUID patientId, UUID doctorId);
}