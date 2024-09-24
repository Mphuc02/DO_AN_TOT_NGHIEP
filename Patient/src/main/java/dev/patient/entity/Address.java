package dev.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.UUID;

@Entity
@Table(name = "tbl_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private String street;
    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;

    @OneToOne
    private Patient patient;
}