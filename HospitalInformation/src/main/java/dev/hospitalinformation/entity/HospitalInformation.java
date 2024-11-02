package dev.hospitalinformation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_hospital_information")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HospitalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;
    private String streetName;
}