package dev.faculty.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_faculty")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String facultyDescribe;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "faculty")
    private List<Position> positions;
}