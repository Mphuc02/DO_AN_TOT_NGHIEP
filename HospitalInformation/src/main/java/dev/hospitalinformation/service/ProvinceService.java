package dev.hospitalinformation.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.CheckAddressRequest;
import dev.common.dto.response.address.CommuneResponse;
import dev.common.dto.response.address.DistrictResponse;
import dev.common.exception.NotFoundException;
import dev.common.dto.response.address.ProvinceResponse;
import dev.hospitalinformation.repository.CommuneRepository;
import dev.hospitalinformation.repository.DistrictRepository;
import dev.hospitalinformation.repository.ProvinceRepository;
import dev.hospitalinformation.util.CommuneUtil;
import dev.hospitalinformation.util.DistrictUtil;
import dev.hospitalinformation.util.ProvinceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;
    private final ProvinceUtil provinceUtil;
    private final DistrictUtil districtUtil;
    private final CommuneUtil communeUtil;

    public boolean checkAddress(CheckAddressRequest request){
        return provinceRepository.checkAddress(request.getProvinceId().toString(),
                                                    request.getDistrictId().toString(),
                                                    request.getCommuneId().toString()) > 0;
    }

    public List<ProvinceResponse> getAll(){
        return provinceUtil.listEntityToResponse(provinceRepository.findAll());
    }

    public List<DistrictResponse> findByProvinceId(UUID provinceId){
        return districtUtil.listEntityToResponse(districtRepository.findByProvinceId(provinceId));
    }

    public List<CommuneResponse> findByDistrictId(UUID districtId){
        return communeUtil.listEntityToResponse(communeRepository.findByDistrictId(districtId));
    }

    public ProvinceResponse getById(UUID id){
        return provinceUtil.entityToResponse(provinceRepository.findById(id)
                                                .orElseThrow(() ->
                                                            new NotFoundException(HOSPITAL_INFORMATION_EXCEPTION.PROVINCE_ID_NOT_FOUND)));
    }

//    @Transactional
//    public void saveAll(List<Province> provinces){
//        provinces.forEach(province -> province.getDistricts().forEach(district -> {
//            district.setProvince(province);
//            district.getCommunes().forEach(commune -> commune.setDistrict(district));
//        }));
//        provinceRepository.saveAll(provinces);
//    }
}