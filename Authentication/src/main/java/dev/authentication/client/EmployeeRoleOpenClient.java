package dev.authentication.client;

import dev.common.constant.ApiConstant.*;
import dev.common.model.Permission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;

@FeignClient(name = EMPLOYEE_URL.SERVICE_NAME, path = EMPLOYEE_URL.ROLE_URL)
public interface EmployeeRoleOpenClient {
    @GetMapping(EMPLOYEE_URL.ID)
    List<Permission> getAllRolesOfEmployee(@PathVariable UUID id);
}