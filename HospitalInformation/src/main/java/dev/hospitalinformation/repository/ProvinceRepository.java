package dev.hospitalinformation.repository;

import dev.hospitalinformation.dto.GetAddressDTO;
import dev.hospitalinformation.dto.request.GetAddressDetailRequest;
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
    int checkAddress(String provinceId, String districtId, String communeId);

    @Query(value = """
        Select p.name as provinceName,
                d.name as districtName,
                c.name as communeName
                from Province p
        join District d on p.id = d.province.id
        join Commune c on d.id = c.district.id
        where p.id = :#{#request.provinceId}
        and d.id = :#{#request.districtId}
        and c.id = :#{#request.communeId}
    """)
    GetAddressDTO getAddressDetail(GetAddressDetailRequest request);
}