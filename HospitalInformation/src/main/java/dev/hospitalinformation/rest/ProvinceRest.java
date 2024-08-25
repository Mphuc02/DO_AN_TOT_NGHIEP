package dev.hospitalinformation.rest;

import dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import dev.hospitalinformation.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(PROVINCE_URL.URL)
public class ProvinceRest {
    private final ProvinceService provinceService;

    @GetMapping(PROVINCE_URL.CHECK_ADDRESS)
    public ResponseEntity<Boolean> checkAddress(@RequestParam(PROVINCE_URL.PROVINCE_ID_PARAM) UUID provinceId,
                                                @RequestParam(PROVINCE_URL.DISTRICT_ID_PARAM) UUID districtId,
                                                @RequestParam(PROVINCE_URL.COMMUNE_ID_PARAM) UUID communeId){
        return ResponseEntity.ok(provinceService.checkAddress(provinceId, districtId, communeId));
    }
}