package dev.hospitalinformation.rest;

import dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import dev.hospitalinformation.service.CommuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMUNE_URL.URL)
public class CommuneRest {
    private final CommuneService communeService;

    @GetMapping(COMMUNE_URL.DISTRICT_ID)
    public ResponseEntity<Object> getAllByDistrictId(@PathVariable UUID id){
        //Todo: Hoàn thiện hàm này
        return null;
    }
}