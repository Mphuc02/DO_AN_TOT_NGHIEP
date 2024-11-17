package dev.common.client;

import static dev.common.constant.ApiConstant.EmployeeUrl.*;
import dev.common.model.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;

@FeignClient(name = SERVICE_NAME, path = ROLE_URL)
public interface RoleClient {
    @GetMapping(ID)
    List<Role> getAllRolesOfEmployee(@PathVariable UUID id);
}