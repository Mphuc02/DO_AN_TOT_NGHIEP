package dev.common.dto.response.patient;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FullNameResponse {
    private String firstName;
    private String middleName;
    private String lastName;
}