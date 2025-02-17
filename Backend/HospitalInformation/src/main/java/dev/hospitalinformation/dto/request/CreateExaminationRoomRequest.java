package dev.hospitalinformation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExaminationRoomRequest {
    @NotEmpty(message = "Tên phòng không được bỏ trống")
    private String name;
}