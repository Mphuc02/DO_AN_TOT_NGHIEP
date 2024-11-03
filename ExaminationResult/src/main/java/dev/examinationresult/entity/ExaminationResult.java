package dev.examinationresult.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_examination_result")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
public class ExaminationResult {
    @Id
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private UUID patientId;

    private UUID workingScheduleId;

    private UUID employeeId;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime examinatedAt;

    private Integer examinedNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "result")
    private List<ExaminationResultDetail> details;
}