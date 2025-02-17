package dev.medicine.repository;

import dev.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Query(value = """
        Select m from Medicine m
        where :q is null or lower(m.name) like :q
     """)
    List<Medicine> searchByName(String q);
}