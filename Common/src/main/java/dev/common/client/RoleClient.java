package dev.common.client;

import static dev.common.constant.ApiConstant.EMPLOYEE_URL.*;
import dev.common.model.Permission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = ROLE_URL)
public interface RoleClient {
    @GetMapping(ID)
    List<Permission> getAllRolesOfEmployee(@PathVariable UUID id);
}