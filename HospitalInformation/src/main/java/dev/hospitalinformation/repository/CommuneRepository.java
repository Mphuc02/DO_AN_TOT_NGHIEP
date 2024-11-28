package dev.hospitalinformation.repository;

import dev.hospitalinformation.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommuneRepository extends JpaRepository<Commune, UUID> {
    List<Commune> findByDistrictId(UUID districtId);
}