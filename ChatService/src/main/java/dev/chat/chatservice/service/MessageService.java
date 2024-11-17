package dev.chat.chatservice.service;

import dev.chat.chatservice.dto.request.CreateMessageRequest;
import dev.chat.chatservice.dto.request.DetectImageRequest;
import dev.chat.chatservice.entity.Message;
import dev.chat.chatservice.mapper.MessageMapper;
import dev.chat.chatservice.repository.MessageRepository;
import static dev.common.constant.KafkaTopicsConstrant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.chat.DetectedImageChatResponse;
import dev.common.dto.response.chat.MessageResponse;
import dev.common.exception.BaseException;
import dev.common.model.ErrorField;
import dev.common.util.AuditingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final AuditingUtil auditingUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(REQUEST_DETECT_IMAGE_TOPIC)
    private String requestDetectImageTopic;

    public Page<MessageResponse> getWithOtherById(UUID receiverId, Pageable pageable){
        UUID senderId = auditingUtil.getUserLogged().getId();
        Page<Message> messages = messageRepository.findByReceiverId(receiverId, senderId, pageable);
        return messages.map(messageMapper::mapEntityToResponse);
    }

    @Transactional
    public MessageResponse create(CreateMessageRequest request){
        if(!StringUtils.hasText(request.getImage()) && !StringUtils.hasText(request.getContent())){
            ErrorField error = new ErrorField(ChatException.CONTENT_MUST_NOT_EMPTY, CreateMessageRequest.Fields.content);
            throw BaseException.buildBadRequest().addField(error).build();
        }

        Message message = messageMapper.mapCreateRequestToEntity(request);
        UUID senderId = auditingUtil.getUserLogged().getId();
        message.setSenderId(senderId);
        message.setCreatedAt(LocalDateTime.now());
        message = messageRepository.save(message);

        if(StringUtils.hasText(message.getImage())){
            DetectImageRequest detectRequest = messageMapper.mapEntityToImageRequest(message);
            kafkaTemplate.send(requestDetectImageTopic, detectRequest);
        }

        return messageMapper.mapEntityToResponse(message);
    }

    @KafkaListener(topics = DETECTED_IMAGE_TOPIC, groupId = CHAT_GROUP)
    public void handle(DetectedImageChatResponse response){
        log.info("Request response from detected_image topic with id: " + response.getId());
        Message message = messageRepository.findById(response.getId()).orElseThrow(() -> BaseException.buildNotFound().message(ChatException.MESSAGE_NOT_FOUND).build());
        message.setProcessedImage(response.getProcessedImage());
        messageRepository.save(message);
    }
}