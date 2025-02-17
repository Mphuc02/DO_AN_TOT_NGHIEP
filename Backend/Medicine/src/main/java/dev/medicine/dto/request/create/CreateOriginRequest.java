package dev.medicine.dto.request.create;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateOriginRequest {
    @NotEmpty(message = "Tên nguồn gốc không được bỏ trống")
    private String name;

    @NotEmpty(message = "Mô tả của nguồn gốc không được bỏ trống")
    private String description;
}