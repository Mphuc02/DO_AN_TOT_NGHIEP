package dev.greeting.repository;

import dev.greeting.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query("Select t from Ticket t where t.date >= :today and t.date < :tomorrow")
    Ticket getByToday(Date today, Date tomorrow);
}