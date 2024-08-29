package dev.employee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.UUID;

@Entity
@Table(name = "tbl_name")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FullName {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;

    @OneToOne
    private Employee employee;
}