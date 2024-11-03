package dev.websocket.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.response.examination_form.ExaminationFormResponse;
import dev.common.model.ProcessedImageData;
import dev.websocket.constant.WebsocketConstant.*;
import dev.websocket.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = CREATE_EMPLOYEE_TOPIC, groupId = WEBSOCKET_GROUP)
    public void handle(CommonRegisterEmployeeRequest request) {
        log.info("Received message from topic: create-employee-account with owner:" +request.getOwner());
        Message message = Message.buildOkMessage(MESSAGE.CREATED_EMPLOYEE_ACCOUNT);
        this.messagingTemplate.convertAndSend(TOPIC.CREATE_EMPLOYEE_TOPIC(request.getOwner()), message);
    }

    @KafkaListener(topics = CREATED_EMPLOYEE_TOPIC, groupId = WEBSOCKET_GROUP)
    public void handle(UUID ownerId){
        log.info("Received message from topic: created-employee-topic with owner: " + ownerId);
        Message message = Message.buildOkMessage(MESSAGE.CREATED_EMPLOYEE);
        this.messagingTemplate.convertAndSend(TOPIC.CREATED_EMPLOYEE_TOPIC(ownerId), message);
    }

    @KafkaListener(topics = PROCESSED_IMAGE, groupId = WEBSOCKET_GROUP)
    public void handle(ProcessedImageData data){
        log.info("Received message from topic: processed-image with owner:" + data.getOwner());
        Message message = Message.buildOkMessage(MESSAGE.PROCESSED_IMAGE, data);
        messagingTemplate.convertAndSend(TOPIC.PROCESSED_IMAGE(data.getOwner()), message);
    }

    @KafkaListener(topics = UPDATED_NUMBER_EXAMINATION_FORM, groupId = WEBSOCKET_GROUP)
    public void handle(ExaminationFormResponse data){
        log.info("Received message from topic: updated_number_examination_form" + data.getEmployeeId());
        Message message = Message.buildOkMessage(MESSAGE.UPDATED_NUMBER_EXAMINATION_FORM, data);
        messagingTemplate.convertAndSend(TOPIC.UPDATED_NUMBER_EXAMINATION_FORM(data.getEmployeeId()), message);
    }
}