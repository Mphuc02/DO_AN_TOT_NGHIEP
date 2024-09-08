package dev.authentication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "tbl_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account implements UserDetails {
    @Id
    @JdbcType(VarcharJdbcType.class)
    public UUID id;
    private String passWord;

    @Column(columnDefinition = "varchar(20) unique")
    private String userName;

    @Column(columnDefinition = "varchar(20) unique")
    private String numberPhone;

    @Column(columnDefinition = "varchar(255) unique")
    private String email;
    private Date createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}