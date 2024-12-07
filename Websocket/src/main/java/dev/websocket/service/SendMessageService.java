package dev.websocket.service;

import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.dto.request.RegisterEmployeeCommonRequest;
import dev.common.dto.response.chat.MessageResponse;
import dev.common.dto.response.examination_form.ExaminationFormResponse;
import dev.common.dto.response.payment.InvoiceResponse;
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
    public void handle(RegisterEmployeeCommonRequest request) {
        log.info("Received message from topic: create-employee-account with owner:" +request.getOwner());
        Message message = Message.buildOkMessage(MessageTemplate.CREATED_EMPLOYEE_ACCOUNT);
        this.messagingTemplate.convertAndSend(Topic.createEmployeeTopic(request.getOwner()), message);
    }

    @KafkaListener(topics = CREATED_EMPLOYEE_TOPIC, groupId = WEBSOCKET_GROUP)
    public void handle(UUID ownerId){
        log.info("Received message from topic: created-employee-topic with owner: " + ownerId);
        Message message = Message.buildOkMessage(MessageTemplate.CREATED_EMPLOYEE);
        this.messagingTemplate.convertAndSend(Topic.createdEmployeeTopic(ownerId), message);
    }

    @KafkaListener(topics = PROCESSED_IMAGE, groupId = WEBSOCKET_GROUP)
    public void handle(ProcessedImageData data){
        log.info("Received message from topic: processed-image with owner:" + data.getOwner());
        Message message = Message.buildOkMessage(MessageTemplate.PROCESSED_IMAGE, data);
        messagingTemplate.convertAndSend(Topic.processedImageTopic(data.getOwner()), message);
    }

    @KafkaListener(topics = UPDATED_NUMBER_EXAMINATION_FORM_TOPIC, groupId = WEBSOCKET_GROUP)
    public void handle(ExaminationFormResponse data){
        log.info("Received message from topic: updated_number_examination_form with id: " + data.getEmployeeId());
        Message message = Message.buildOkMessage(MessageTemplate.UPDATED_NUMBER_EXAMINATION_FORM, data);
        messagingTemplate.convertAndSend(Topic.updatedNumberExaminationFormTopic(data.getEmployeeId()), message);
    }

    @KafkaListener(topics = NEW_MESSAGE_TOPIC, groupId = WEBSOCKET_GROUP)
    public void handle(MessageResponse response){
        log.info("Received message from topic: new_message with relationShipId: " + response.getRelationShipId());
        Message message = Message.buildOkMessage(MessageTemplate.NEW_MESSAGE, response);
        messagingTemplate.convertAndSend(Topic.chattingTopic(response.getReceiverId()), message);
        messagingTemplate.convertAndSend(Topic.chattingTopic(response.getSenderId()), message);
    }

    @KafkaListener(topics = COMPLETED_PAYMENT_INVOICE, groupId = WEBSOCKET_GROUP)
    public void handle(InvoiceResponse invoiceResponse){
        log.info("Received message from topic: completed_payment_invoice with ownerId: " + invoiceResponse.getEmployeeId());
        Message message = Message.buildOkMessage(MessageTemplate.COMPLETED_PAYMENT_INVOICE);
        messagingTemplate.convertAndSend(Topic.completedInvoice(invoiceResponse.getEmployeeId()), message);
    }
}