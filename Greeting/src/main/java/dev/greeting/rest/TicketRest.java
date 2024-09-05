package dev.greeting.rest;

import dev.common.constant.ApiConstant.*;
import dev.greeting.entity.Ticket;
import dev.greeting.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GREETING_URL.TICKET_URL)
@RequiredArgsConstructor
public class TicketRest {
    private final TicketService ticketService;
    private final Ticket todayTicket;

    @GetMapping(GREETING_URL.PRINT)
    public ResponseEntity<Object> printTicket(){
        ticketService.printTicket();
        return ResponseEntity.ok(todayTicket.getTotal());
    }
}