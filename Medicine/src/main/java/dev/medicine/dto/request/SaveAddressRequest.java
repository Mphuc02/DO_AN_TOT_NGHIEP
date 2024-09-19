package dev.medicine.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveAddressRequest {
    @NotEmpty(message = "Tên phố không được bỏ trống")
    private String street;

    @NotNull(message = "Tỉnh không được bỏ trống")
    private UUID provinceId;

    @NotNull(message = "Huyện không được bỏ trống")
    private UUID districtId;

    @NotNull(message = "Xã không được bỏ trống")
    private UUID communeId;
}