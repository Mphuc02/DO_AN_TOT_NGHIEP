package dev.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_examination_cost")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationCost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal cost;
    private LocalDate appliedAt;
}