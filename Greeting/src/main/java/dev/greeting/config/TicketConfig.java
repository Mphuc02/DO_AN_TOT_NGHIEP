package dev.greeting.config;

import dev.greeting.entity.Ticket;
import dev.greeting.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Date;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TicketConfig {
    private final TicketRepository ticketRepository;
    @Bean
    public Ticket getTicketOfToday(){
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        Date tomorrow = new Date(today.getTime() + 1000 * 60 * 60 *24);

        log.info("Searching ticket for today %s-%s-%s");
        return ticketRepository.getByToday(today, tomorrow);
    }
}