package dev.hospitalinformation.service;

import dev.hospitalinformation.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    public boolean checkAddress(UUID provinceId, UUID districtId, UUID communeId){
        return provinceRepository.checkAddress(provinceId, districtId, communeId) > 0;
    }
}