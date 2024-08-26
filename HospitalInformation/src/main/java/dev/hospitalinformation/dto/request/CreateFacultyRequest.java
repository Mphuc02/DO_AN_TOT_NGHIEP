package dev.hospitalinformation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateFacultyRequest {
    @NotEmpty(message = "Tên khoa không được bỏ trống")
    @Size(max = 255, message = "Tên khoa không được vượt quá 255 ký tự")
    private String name;

    @NotEmpty(message = "mô tả không được bỏ trống")
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String describe;
}