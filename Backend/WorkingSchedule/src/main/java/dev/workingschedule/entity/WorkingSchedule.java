package dev.workingschedule.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_working_schedule")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private UUID roomId;

    private UUID employeeId;

    private LocalDate date;
}