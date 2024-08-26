package dev.hospitalinformation.util;

import dev.hospitalinformation.entity.Province;
import dev.common.dto.response.ProvinceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProvinceUtil {
    private final DistrictUtil districtUtil;

    public ProvinceResponse entityToResponse(Province province){
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .districts(districtUtil.listEntityToResponse(province.getDistricts()))
                .build();
    }

    public List<ProvinceResponse> listEntityToResponse(List<Province> provinces){
        return provinces.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}