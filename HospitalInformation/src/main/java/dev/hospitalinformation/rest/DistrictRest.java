package dev.hospitalinformation.rest;

import dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.DISTRICT_URL;
import dev.hospitalinformation.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(DISTRICT_URL.URL)
public class DistrictRest {
    private final DistrictService districtService;

    @GetMapping(DISTRICT_URL.PROVINCE_ID)
    public ResponseEntity<Object> getAllByProvinceId(@PathVariable UUID id){
        //Todo: Hoàn thiện hàm này
        return null;
    }
}