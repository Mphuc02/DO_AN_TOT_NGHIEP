package dev.hospitalinformation.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateExaminationRoomRequest {
    private String name;
}