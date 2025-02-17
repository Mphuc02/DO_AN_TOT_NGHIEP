package dev.common.client;

import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = DISEASE_URL)
public interface DiseaseClient {
    @PostMapping(CHECK_DISEASES_EXIST)
    List<UUID> checkDiseasesExist(@RequestBody List<UUID> diseaseIds);
}