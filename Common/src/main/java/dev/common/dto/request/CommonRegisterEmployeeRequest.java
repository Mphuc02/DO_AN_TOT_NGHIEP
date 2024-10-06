package dev.common.dto.request;

import dev.common.model.Permission;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommonRegisterEmployeeRequest {
    private UUID id;
    private CreateFullNameRequest fullName;
    private String introduce;
    private Date dateOfBirth;
    private List<Permission> permissions;
    private UUID owner;
}