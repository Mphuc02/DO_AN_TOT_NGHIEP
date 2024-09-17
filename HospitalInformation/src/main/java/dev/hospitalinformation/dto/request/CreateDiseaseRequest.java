package dev.hospitalinformation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateDiseaseRequest {
    @NotEmpty(message = "Tên bệnh không được bỏ trống")
    @Length(min = 5, max = 255, message = "Tên bệnh chỉ được phép từ 5 đến 255 ký tự")
    private String name;

    @NotEmpty(message = "Mô tả của bệnh không được bỏ trống")
    @Length(min = 5, max = 1000, message = "Mô tả của bệnh chỉ được phép chứa 5 đến 1000 ký tự")
    private String description;
}