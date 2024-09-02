package dev.common.dto.response;

import dev.common.model.Permission;
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
    private FullNameResponse fullName;
    private String introduce;
    private Date date;
    private Set<Permission> permissions;
}