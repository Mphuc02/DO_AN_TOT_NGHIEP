package dev.faculty.entity;


import dev.common.model.Permission;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.UUID;

@Entity
@Table(name = "tbl_position")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private Integer quantity;

    @Enumerated
    private Permission permission;

    @ManyToOne
    private Faculty faculty;
}