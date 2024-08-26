package dev.hospitalinformation.util;

import dev.hospitalinformation.entity.Commune;
import dev.common.dto.response.CommuneResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommuneUtil {
    public CommuneResponse entityToResponse(Commune entity){
        return CommuneResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<CommuneResponse> listEntityToResponse(List<Commune> communes){
        return communes.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}