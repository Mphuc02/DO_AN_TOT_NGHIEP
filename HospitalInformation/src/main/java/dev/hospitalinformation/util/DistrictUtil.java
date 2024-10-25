package dev.hospitalinformation.util;

import dev.hospitalinformation.entity.District;
import dev.common.dto.response.address.DistrictResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DistrictUtil {
    private final CommuneUtil communeUtil;

    public DistrictResponse entityToResponse(District entity){
        return DistrictResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
//                .communes(communeUtil.listEntityToResponse(entity.getCommunes()))
                .build();
    }

    public List<DistrictResponse> listEntityToResponse(List<District> districts){
        return districts.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}