package dev.medicine.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_medicine")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Origin origin;
}