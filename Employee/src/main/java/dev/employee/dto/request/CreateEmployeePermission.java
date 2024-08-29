package dev.employee.dto.request;

import dev.common.validation.NotAdminPermissionValidation;
import lombok.*;
import java.security.Permission;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateEmployeePermission {
    @NotAdminPermissionValidation
    private Permission permission;
}