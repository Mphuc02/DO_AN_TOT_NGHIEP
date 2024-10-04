package dev.authentication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "tbl_invalid_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvalidToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Timestamp time;
}