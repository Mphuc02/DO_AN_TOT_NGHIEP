package dev.greeting.service;

import dev.greeting.entity.Ticket;
import dev.greeting.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {
     private final TicketRepository ticketRepository;
     private final Ticket todayTicket;

     public void printTicket(){
          todayTicket.increaseTotal();
          ticketRepository.save(todayTicket);
     }
}