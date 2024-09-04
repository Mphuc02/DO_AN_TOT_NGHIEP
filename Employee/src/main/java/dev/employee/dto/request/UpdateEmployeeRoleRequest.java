package dev.employee.dto.request;

import dev.common.model.Permission;
import dev.common.validation.NotAdminPermissionValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEmployeeRoleRequest {
    @NotNull(message = "Chức vụ không được bỏ trống")
    @NotAdminPermissionValidation
    private Permission permission;
}
