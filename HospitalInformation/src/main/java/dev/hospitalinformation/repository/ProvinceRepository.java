package dev.hospitalinformation.repository;

import dev.hospitalinformation.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    @Query(value = """
                   SELECT exists(
                        SELECT p.*
                            FROM tbl_province p
                        JOIN tbl_district d
                            ON d.province_id = p.id
                            AND d.id = :districtId
                       JOIN tbl_commune c
                            On c.district_id = d.id
                            AND c.id = :communeId
                       WHERE
                            p.id = :provinceId
                   )""", nativeQuery = true)
    int checkAddress(UUID provinceId, UUID districtId, UUID communeId);
}