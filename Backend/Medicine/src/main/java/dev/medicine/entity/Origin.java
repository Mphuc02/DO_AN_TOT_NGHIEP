package dev.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_origin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "origin")
    private List<Medicine> medicines;
}