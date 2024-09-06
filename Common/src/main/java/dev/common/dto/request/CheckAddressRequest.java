package dev.common.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckAddressRequest {
    @NotNull(message = "Tỉnh không đuợc bỏ trống")
    private UUID provinceId;

    @NotNull(message = "Huyện không được bỏ trống")
    private UUID districtId;

    @NotNull(message = "Xã không được bỏ trống")
    private UUID communeId;
}