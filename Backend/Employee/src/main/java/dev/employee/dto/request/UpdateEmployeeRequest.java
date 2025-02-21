package dev.employee.dto.request;

import dev.common.dto.request.UpdateWithFullNameRequest;
import dev.common.validator.DateOfBirthValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateEmployeeRequest {
    @Size(max = 1000, message = "Giới thiệu về nhân viên không được quá 1000 ký tự")
    private String introduce;

    @DateOfBirthValidator(minimumAge = 18, message = "Tuổi phải ít nhất 18 tuổi")
    private Date dateOfBirth;

    @Valid
    private UpdateWithFullNameRequest fullName;

    @Valid
    private List<UpdateEmployeeRoleRequest> addRoles;

    @Valid
    private List<UpdateEmployeeRoleRequest> deleteRoles;
}