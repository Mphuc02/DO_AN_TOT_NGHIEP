package dev.patient.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAppointmentImageDetailRequest {
    @NotEmpty(message = "Hình ảnh không được bỏ trống")
    private String image;

    @NotEmpty(message = "Hình ảnh chuẩn đoán không được bỏ trống")
    private String processedImage;
}