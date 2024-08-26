package dev.hospitalinformation.service;

import dev.hospitalinformation.entity.Province;
import dev.common.dto.response.ProvinceResponse;
import dev.hospitalinformation.repository.ProvinceRepository;
import dev.hospitalinformation.util.ProvinceUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final ProvinceUtil provinceUtil;

    public boolean checkAddress(UUID provinceId, UUID districtId, UUID communeId){
        return provinceRepository.checkAddress(provinceId, districtId, communeId) > 0;
    }

    public List<ProvinceResponse> getAll(){
        return provinceUtil.listEntityToResponse(provinceRepository.findAll());
    }

    public ProvinceResponse getById(UUID id){
        return provinceUtil.entityToResponse(provinceRepository.findById(id).get());
    }

    @Transactional
    public void saveAll(List<Province> provinces){
        provinces.forEach(province -> province.getDistricts().forEach(district -> {
            district.setProvince(province);
            district.getCommunes().forEach(commune -> commune.setDistrict(district));
        }));
        provinceRepository.saveAll(provinces);
    }
}