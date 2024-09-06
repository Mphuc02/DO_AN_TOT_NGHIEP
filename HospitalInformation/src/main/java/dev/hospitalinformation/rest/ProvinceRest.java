package dev.hospitalinformation.rest;

import com.google.gson.Gson;
import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;

import dev.common.dto.request.CheckAddressRequest;
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
@RequestMapping(URL)
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

    @PostMapping(CHECK_ADDRESS)
    public ResponseEntity<Boolean> checkAddress(@RequestBody CheckAddressRequest request){
        return ResponseEntity.ok(provinceService.checkAddress(request));
    }

//    @GetMapping("/test")
//    public String test() throws IOException {
//        Gson gson = new Gson();
//        ClassPathResource resource = new ClassPathResource("static/out.json");
//        BufferedReader rd = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//        String line = rd.readLine();
//        StringBuilder result = new StringBuilder(line);
//        while((line = rd.readLine()) != null){
//            result.append(line);
//        }
//
//        List<Province> provinces = Arrays.asList(gson.fromJson(result.toString(), Province[].class));
//        provinceService.saveAll(provinces);
//        return "ok";
//    }
}