package dev.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Service
@RestController()
@RequestMapping("/test")
@RequiredArgsConstructor
public class SendMessageService {
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public void broadcastNews() {
        this.messagingTemplate.convertAndSend("/topic/news", "1234");
    }
}