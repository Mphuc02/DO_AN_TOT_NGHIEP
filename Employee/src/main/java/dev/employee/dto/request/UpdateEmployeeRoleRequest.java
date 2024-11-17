package dev.employee.dto.request;

import dev.common.model.Role;
import dev.common.validator.NotAdminPermissionValidator;
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
    @NotAdminPermissionValidator
    private Role role;
}
