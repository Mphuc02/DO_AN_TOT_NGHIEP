package dev.common.dto.request;

import dev.common.model.Permission;
import dev.common.validator.NotAdminPermissionValidator;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatePermissionRequest {
    @NotNull(message = "Chức vụ không được bỏ trống")
    @NotAdminPermissionValidator
    private Permission permission;
}