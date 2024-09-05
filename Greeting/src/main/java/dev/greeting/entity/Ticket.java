package dev.greeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tbl_ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private Integer total;
    private Date date;
    private Integer currentCall;

    public void increaseTotal(){
        this.total++;
    }
}