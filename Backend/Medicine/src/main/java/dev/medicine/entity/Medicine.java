package dev.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
    private BigDecimal price;

    private Integer quantity = 0;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Origin origin;

    public Medicine(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(id, medicine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void subtractQuantity(int quantity){
        this.quantity -= quantity;
    }
    public void increaseQuantity(int quantity){
        this.quantity += quantity;
    }
}