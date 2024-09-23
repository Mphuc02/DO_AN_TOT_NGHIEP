package dev.medicine.dto.request.update;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateOriginRequest {
    private String name;
    private String description;
}