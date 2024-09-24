package dev.common.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FullNameCommonResponse {
    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
}