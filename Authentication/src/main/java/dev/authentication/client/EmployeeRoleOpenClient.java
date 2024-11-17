package dev.authentication.client;

import dev.common.constant.ApiConstant.*;
import dev.common.model.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;

@FeignClient(name = EmployeeUrl.SERVICE_NAME, path = EmployeeUrl.ROLE_URL)
public interface EmployeeRoleOpenClient {
    @GetMapping(EmployeeUrl.ID)
    List<Role> getAllRolesOfEmployee(@PathVariable UUID id);
}