package dev.authentication.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime invalidatedAt;
}