package dev.hospitalinformation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import java.util.UUID;

@Entity
@Table(name = "tbl_commune")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private String name;

    @ManyToOne
    private District district;
}