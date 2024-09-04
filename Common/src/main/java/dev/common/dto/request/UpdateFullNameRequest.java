package dev.common.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateFullNameRequest {
    @NotNull(message = "Id của tên không được bỏ trống")
    private UUID id;

    @Size(max = 100, message = "Tên được phép tối đa 100 ký tự")
    private String firstName;

    @Size(max = 100, message = "Tên đệm được phép tối đa 100 ký tự")
    private String middleName;

    @Size(max = 100, message = "Họ được phép tối đa 100 ký tự")
    private String lastName;
}