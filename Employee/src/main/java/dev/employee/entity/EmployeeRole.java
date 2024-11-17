package dev.employee.entity;

import dev.common.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import java.util.UUID;

@Entity
@Table(name = "tbl_employee_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    @Enumerated()
    private Role role;

    @ManyToOne
    private Employee employee;
}