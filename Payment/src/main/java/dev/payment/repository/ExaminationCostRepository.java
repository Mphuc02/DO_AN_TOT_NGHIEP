package dev.payment.repository;

import dev.payment.entity.ExaminationCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface ExaminationCostRepository extends JpaRepository<ExaminationCost, UUID> {
    @Query(value = """
                select ec from ExaminationCost ec
                where ec.appliedAt <= :date
                order by ec.appliedAt
                limit 1
            """)
    ExaminationCost findLatestCostApplied(LocalDate date);
    boolean existsByAppliedAt(LocalDate date);
}
