package dev.greeting.service;

import dev.greeting.entity.Ticket;
import dev.greeting.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
     private final TicketRepository ticketRepository;

     public Ticket getTodayTicket(){
          Date today = new Date();
          today.setHours(0);
          today.setMinutes(0);
          today.setSeconds(0);
          Date tomorrow = new Date(today.getTime() + 1000 * 60 * 60 *24);

          log.info("Searching ticket for today %s-%s-%s");
          return ticketRepository.getByToday(today, tomorrow);
     }

     public int printTicket(){
          Ticket ticket = getTodayTicket();
          ticket.increaseTotal();
          ticket = ticketRepository.save(ticket);
          return ticket.getTotal();
     }
}