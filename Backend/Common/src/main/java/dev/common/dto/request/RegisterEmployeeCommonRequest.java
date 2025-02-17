package dev.common.dto.request;

import dev.common.model.Role;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterEmployeeCommonRequest {
    private UUID id;
    private CreateWithFullNameCommonRequest fullName;
    private String introduce;
    private Date dateOfBirth;
    private List<Role> roles;
    private UUID owner;
}