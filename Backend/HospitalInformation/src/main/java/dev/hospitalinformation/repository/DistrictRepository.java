package dev.hospitalinformation.repository;

import dev.hospitalinformation.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, UUID> {
    List<District> findByProvinceId(UUID provinceId);
}