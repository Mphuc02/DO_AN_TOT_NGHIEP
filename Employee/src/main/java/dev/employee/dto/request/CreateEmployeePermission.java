package dev.employee.dto.request;

import dev.common.model.Permission;
import dev.common.validation.NotAdminPermissionValidation;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateEmployeePermission {
    @NotAdminPermissionValidation
    private Permission permission;
}