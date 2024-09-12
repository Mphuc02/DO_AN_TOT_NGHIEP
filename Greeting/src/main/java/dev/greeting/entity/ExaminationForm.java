package dev.greeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "tbl_examination_form")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private UUID patientId;
    private UUID employeeId;
    private Integer ticketIndex;

    private UUID workingScheduleId;

    private Date createdAt;

    @Column(columnDefinition = "TEXT")
    private String symptom;
}