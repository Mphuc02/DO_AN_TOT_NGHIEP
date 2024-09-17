package dev.hospitalinformation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_disease")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "Text")
    private String description;
}