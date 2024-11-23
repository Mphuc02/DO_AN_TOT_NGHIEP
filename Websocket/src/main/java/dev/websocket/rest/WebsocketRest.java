package dev.websocket.rest;

import dev.common.util.AuditingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static dev.common.constant.KafkaTopicsConstrant.RECEIVED_IMAGE_MESSAGE;

@RestController
@RequiredArgsConstructor
public class WebsocketRest {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuditingUtil auditingUtil;
    @Value(RECEIVED_IMAGE_MESSAGE)
    private String receivedImageMessage;

    @MessageMapping("/chat/image/patient/{id}")
    public void handleReceivedImageMessagePatient(@PathVariable UUID userId, @Header("senderId") String senderId, @Payload String image){
        System.out.println(senderId);
        System.out.println(userId);
        System.out.println(image);
    }

    @MessageMapping("/chat/image/doctor/{id}")
    public void handleReceivedImageMessageDoctor(@PathVariable UUID userId, @Header("senderId") String senderId, @Payload String image){
        System.out.println(senderId);
        System.out.println(userId);
        System.out.println(image);
    }
}