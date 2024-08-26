package dev.authentication.entity;

import dev.authentication.model.Permission;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import java.util.UUID;

@Entity
@Table(name = "tbl_account_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    @Enumerated
    private Permission permission;

    @ManyToOne
    private Account account;
}