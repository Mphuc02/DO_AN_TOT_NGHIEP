package dev.hospitalinformation.rest;

import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import static dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.CheckAddressCommonRequest;
import dev.common.dto.response.address.AddressResponse;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.hospitalinformation.dto.request.GetAddressDetailRequest;
import dev.hospitalinformation.service.ProvinceService;
import feign.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(PROVINCE_URL)
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

    @GetMapping("/by-province/{id}")
    public ResponseEntity<Object> findDistrictsByProvinceId(@PathVariable UUID id){
        return ResponseEntity.ok(provinceService.findByProvinceId(id));
    }

    @GetMapping("/by-district/{id}")
    public ResponseEntity<Object> findCommunesByDistrictId(@PathVariable UUID id){
        return ResponseEntity.ok(provinceService.findByDistrictId(id));
    }

    @PostMapping(CHECK_ADDRESS)
    public ResponseEntity<Boolean> checkAddress(@Valid @RequestBody CheckAddressCommonRequest request,
                                                BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), HOSPITAL_INFORMATION_EXCEPTION.FAIL_CHECK_ADDRESS);
        }
        return ResponseEntity.ok(provinceService.checkAddress(request));
    }

    @PostMapping(GET_ADDRESS_DETAIL)
    public ResponseEntity<AddressResponse> getAddressDetail(@RequestBody GetAddressDetailRequest request){
        return ResponseEntity.ok(provinceService.getAddressDetail(request));
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