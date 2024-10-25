package dev.common.dto.response.patient;

import dev.common.dto.response.address.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PatientResponse {
    private UUID id;
    private AddressResponse address;
    private FullNameResponse fullName;
}