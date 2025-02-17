package dev.common.dto.response.employee;

import dev.common.model.Role;
import lombok.*;
import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeResponse {
    private UUID id;
    private FullNameCommonResponse fullName;
    private String introduce;
    private Date date;
    private Set<Role> roles;
}