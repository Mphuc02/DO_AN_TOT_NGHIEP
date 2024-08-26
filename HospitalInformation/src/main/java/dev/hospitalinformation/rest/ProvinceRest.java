package dev.hospitalinformation.rest;

import com.google.gson.Gson;
import dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import dev.hospitalinformation.entity.Province;
import dev.hospitalinformation.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(PROVINCE_URL.URL)
public class ProvinceRest {
    private final ProvinceService provinceService;

    @GetMapping()
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(provinceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id){
        return ResponseEntity.ok(provinceService.getById(id));
    }

    @GetMapping(PROVINCE_URL.CHECK_ADDRESS)
    public ResponseEntity<Boolean> checkAddress(@RequestParam(PROVINCE_URL.PROVINCE_ID_PARAM) UUID provinceId,
                                                @RequestParam(PROVINCE_URL.DISTRICT_ID_PARAM) UUID districtId,
                                                @RequestParam(PROVINCE_URL.COMMUNE_ID_PARAM) UUID communeId){
        return ResponseEntity.ok(provinceService.checkAddress(provinceId, districtId, communeId));
    }

    @GetMapping("/test")
    public String test() throws IOException {
        Gson gson = new Gson();
        ClassPathResource resource = new ClassPathResource("static/out.json");
        BufferedReader rd = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line = rd.readLine();
        StringBuilder result = new StringBuilder(line);
        while((line = rd.readLine()) != null){
            result.append(line);
        }

        List<Province> provinces = Arrays.asList(gson.fromJson(result.toString(), Province[].class));
        provinceService.saveAll(provinces);
        return "ok";
    }
}