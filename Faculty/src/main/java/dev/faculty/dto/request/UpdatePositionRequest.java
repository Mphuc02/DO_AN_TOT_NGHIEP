package dev.faculty.dto.request;

import dev.common.model.Permission;
import dev.common.validator.NotAdminPermissionValidator;
import dev.faculty.validation.FacultyExistValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePositionRequest {
    @NotNull(message = "Số lượng nhân viên không được để trống")
    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private Integer quantity;

    @NotNull(message = "Chức vụ không được bỏ trống")
    @NotAdminPermissionValidator
    private Permission permission;

    @FacultyExistValidation
    private UUID facultyId;
}