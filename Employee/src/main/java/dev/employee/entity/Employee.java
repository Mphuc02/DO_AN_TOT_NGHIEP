package dev.employee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "tbl_emplyee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Employee {
    @Id
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private FullName fullName;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    private Date dateOfBirth;
}