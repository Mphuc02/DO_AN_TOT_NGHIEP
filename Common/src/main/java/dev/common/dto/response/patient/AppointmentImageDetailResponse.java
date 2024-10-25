package dev.common.dto.response.patient;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentImageDetailResponse {
    private String image;
    private String processedImage;
}