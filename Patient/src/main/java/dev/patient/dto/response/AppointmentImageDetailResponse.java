package dev.patient.dto.response;

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