package dev.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateWithFullNameCommonRequest {
    @NotEmpty(message = "Tên không được bỏ trống")
    @Size(max = 100, message = "Tên được phép tối đa 100 ký tự")
    private String firstName;

    @NotEmpty(message = "Tên đệm không được bỏ trống")
    @Size(max = 100, message = "Tên đệm được phép tối đa 100 ký tự")
    private String middleName;

    @NotEmpty(message = "Họ không được bỏ trống")
    @Size(max = 100, message = "Họ được phép tối đa 100 ký tự")
    private String lastName;
}