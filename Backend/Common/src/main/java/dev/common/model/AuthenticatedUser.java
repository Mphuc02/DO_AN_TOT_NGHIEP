package dev.common.model;

import lombok.*;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticatedUser {
    private UUID id;
    private Set<Role> roles;
}