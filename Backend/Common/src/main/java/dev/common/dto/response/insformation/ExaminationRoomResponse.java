package dev.common.dto.response.insformation;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationRoomResponse {
    private UUID id;
    private String name;
}